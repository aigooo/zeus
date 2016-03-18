package com.zeus.persist.hibernate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import com.zeus.common.util.TypeUtil;
import com.zeus.persist.Page;
import com.zeus.persist.model.IdModel;
import com.zeus.persist.model.MarkDelete;


@SuppressWarnings("rawtypes")
public abstract class HibernateBaseDaoImpl<T extends IdModel<K>,K> extends HibernateDaoSupport implements HibernateBaseDao<T,K> {
	
	public abstract void setDaoSessionFactory(SessionFactory sessionFactory);
	
	@SuppressWarnings("unchecked")
	public List<T> sqlQuery(final String sql,Object[] params,Class<T> clazz){
		return this.getHibernateTemplate().execute(session -> {
            SQLQuery sq = session.createSQLQuery(sql);
            for(int i=0;i<params.length;i++){
            	sq.setParameter(i, params[i]);
            }
            sq.addEntity(clazz);
            return (List<T>)sq.list();
        });
	}
	
	@SuppressWarnings("unchecked")
	public List<T> sqlQuery(final String sql,Map<String,Object> params,Class<T> clazz){
		return this.getHibernateTemplate().execute(session -> {
            SQLQuery sq = session.createSQLQuery(sql);
            Iterator<String> it = params.keySet().iterator();
            while(it.hasNext()){
            	String key = it.next();
            	if(Collection.class.isAssignableFrom(params.get(key).getClass())){
            		sq.setParameterList(key, (Collection) params.get(key));
            	}else if(params.getClass().isArray()){
            		sq.setParameterList(key, (Object[]) params.get(key));
            	}else{
            		sq.setParameter(key, params.get(key));
            	}
            }
            sq.addEntity(clazz);
            return (List<T>)sq.list();
        });
	}

	@SuppressWarnings("unchecked")
	public T save(T t) {
		t.setId((K) getHibernateTemplate().save(t));
		return t;
	}

	public int saveBatch(Collection<T> ts) {
		if(ts!=null){
			for(T t:ts){
				getHibernateTemplate().save(t);
			}
		}
		return 0;
	}
	
	public T saveOrUpdate(T t){
		getHibernateTemplate().saveOrUpdate(t);
		return t;
	}
	
	public int saveOrUpdateBatch(Collection<T> ts){
		for(T t:ts){
			getHibernateTemplate().saveOrUpdate(t);
		}
		return 0;
	}

	public T update(T t) {
		getHibernateTemplate().update(t);
		return t;
	}

	public T get(Class<T> clazz,K key) {
		return getHibernateTemplate().get(clazz,(Serializable)key);
	}

	public T delete(T t) {
		getHibernateTemplate().delete(t);
		return t;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getList(T t) {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = createQueryConditionWithOrderby(t).query(session);
		return query.list();
	}

	@Override
	public void hqlExecute(String hql,Object[] objs) {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createQuery(hql);
		if(objs!=null){
			for(int i=0;i<objs.length;i++){
				query.setParameter(i, objs[i]);
			}
		}
		query.executeUpdate();
	}

	public abstract QueryCondition createQueryCondition(T t);

	public QueryCondition createQueryConditionWithOrderby(T t){
		if(t!=null&& StringUtils.isNotBlank(t.getOrder())){
			return createQueryCondition(t).append( " order by ").append(t.getOrder());
		}else{
			return createQueryCondition(t).append( " order by id desc");
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getList(T t,int begin,int length){
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = createQueryConditionWithOrderby(t).query(session);
		query.setFirstResult(begin);
		if(length>0){
			query.setMaxResults(length);
		}
		return query.list();
	}

	@Override
	public T prevGet(T t) {
		t.setOrder(" id desc");
		List<T> list = getList(t, 0, 1);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public T nextGet(T t) {
		t.setOrder(" id ");
		List<T> list = getList(t, 1, 1);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public int sqlExecute(String sql){
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Transaction trs = session.beginTransaction();
		SQLQuery sqlq = session.createSQLQuery(sql);
		int t = sqlq.executeUpdate();
		trs.commit();
		return t;
	}
	
	public void getPage(Page<T> tp,T t){
		tp.setCount(getCount(t));
		tp.setPageCount((tp.getCount()%tp.getPageSize()==0?tp.getCount()/tp.getPageSize():tp.getCount()/tp.getPageSize()+1));
		tp.setList(getList(t,(tp.getPageNbr()-1)*tp.getPageSize(),tp.getPageSize()));
	}
	
	public Integer getCount(T t){
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = createQueryCondition(t).prepend("select count(*) ").query(session);
		return ((Long)query.uniqueResult()).intValue();
	}
	
	public void merge(T t){
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		session.merge(t);
	}
	
	public List sqlList(String sql){
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		SQLQuery sqlq = session.createSQLQuery(sql);
		return sqlq.list();
	}

	@Override
	public Number getAvg(String proName,T t, int begin, int length) {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createQuery("select avg("+proName+") " + createQueryConditionWithOrderby(t));
		query.setFirstResult(begin);
		if(length>0){
			query.setMaxResults(length);
		}
		return (Number)query.uniqueResult();
	}

	@Override
	public Number getMax(String proName,T t, int begin, int length) {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = createQueryConditionWithOrderby(t).prepend(") ").prepend(proName).prepend("select max(").query(session);
		query.setFirstResult(begin);
		if(length>0){
			query.setMaxResults(length);
		}
		return (Number)query.uniqueResult();
	}

	@Override
	public Number getMin(String proName,T t, int begin, int length) {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = createQueryConditionWithOrderby(t).prepend(") ").prepend(proName).prepend("select min(").query(session);
		query.setFirstResult(begin);
		if(length>0){
			query.setMaxResults(length);
		}
		return (Number)query.uniqueResult();
	}
	
	@Override
	public Number getMid(String proName,T t,int begin,int length){
		List<Number> numbers = getList4ProValue(proName,t,begin,length);
		if(numbers!=null&&numbers.size()>0){
			return numbers.get(numbers.size()/2);
		}else{
			return -999999;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Number> getList4ProValue(String proName,T t, int begin, int length) {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		String hql = "select "+proName + createQueryConditionWithOrderby(t);
		Query query = session.createQuery(hql);
		query.setFirstResult(begin);
		if(length>0){
			query.setMaxResults(length);
		}
		return query.list();
	}

	@Override
	public void setDateSource(DataSource dateSource) throws Exception{
		throw new Exception("Setting dateSource is not supported ");
	}

	@Override
	public List getDistinctProValue(String proName,T t,int begin,int length){
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		QueryCondition queryCondition = createQueryConditionWithOrderby(t).prepend(" ").prepend(proName).prepend("select distinct ");
		queryCondition.setSql(new StringBuilder(ignoreOrderBy(queryCondition.getSql().toString())))
					  .append(" AND ?  is not null ").param(proName);
		Query query = queryCondition.query(session);
		query.setFirstResult(begin);
		if(length>0){
			query.setMaxResults(length);
		}
		return query.list();
	}
	
	private String ignoreOrderBy(String hql){
		if(hql.toUpperCase().contains("ORDER BY")){
			hql = hql.substring(0,hql.toUpperCase().indexOf("ORDER BY"));
		}
		return hql;
	}

    @Override
    public int updateBatch(Collection<T> ts) {
        return 0;
    }

    @Override
    public int deleteBatch(Collection<T> ts) {
        return 0;
    }
    
	@Override
	public List<T> getList(String sql, T t, int begin, int length) {
		return null;
	}

	@Override
	public void removeByCondition(T t) {
		QueryCondition queryCondition = createQueryConditionWithOrderby(t);
		if(t instanceof MarkDelete){
			queryCondition.setSql(new StringBuilder(queryCondition.getSql().substring(queryCondition.getSql().indexOf("1=1"))))
					.prepend(" t set t.isDelete = 1 where  ").prepend(t.getClass().getSimpleName()).prepend("  update ");
			hqlExecute(queryCondition.toString(),queryCondition.getParams());
		}else{
			hqlExecute(queryCondition.prepend(" Delete ").toString(),queryCondition.getParams());
		}
	}

	@Override
	public List<T> getList(String sql, Object[] params, int begin, int length) {
		return null;
	}

	@Override
	public List sqlList(String sql, Object[] params) {
		return null;
	}
	
	public List<T> hqlList(String hql, Object[] values) {
		return null;
	}
	
	@Override
	public Object init(Object object) {
    	if(object instanceof Collection){
    		List<Object> result = new ArrayList<>();
    		Iterator it = ((Collection)object).iterator();
    		while(it.hasNext()) {
    			Object item = it.next();
    			initItem(item);
    			result.add(item);
    		}
    		return result;
    	}else{
    		initItem(object);
    	}
    	return object;
	}
    
    @SuppressWarnings("unchecked")
	private void initItem(Object object){
    	Class clazz = object.getClass();
    	Method method = null;
    	try {
			method = clazz.getMethod("getName");
		} catch (NoSuchMethodException | SecurityException e1) {
			try {
				method = clazz.getMethod("getIsDelete");
			} catch (NoSuchMethodException | SecurityException e2) {
				try {
					method = clazz.getMethod("getStatus");
				} catch (NoSuchMethodException | SecurityException e3) {
					try {
						method = clazz.getMethod("getIsEnable");
					} catch (NoSuchMethodException | SecurityException e4) {
						try{
							Field[] fields = clazz.getDeclaredFields();
							String methodName = "";
							for(Field field:fields){
								if(!field.getName().equals("id")&&!field.getName().startsWith("up")&&
										!field.getName().startsWith("dn")&&!field.getName().endsWith("s")&&
										!field.getName().equals("order")&&!field.getName().equals("condition")){
									methodName = TypeUtil.attributeToGetter(field.getName());
									break;
								}
							}
							method = clazz.getMethod(methodName);
						}catch (NoSuchMethodException | SecurityException e5){
							object = null;
						}
					}
				}
			}
		}
    	if(method!=null){
    		try {
				method.invoke(object);
			} catch (IllegalAccessException e) {
				object = null;
			} catch (IllegalArgumentException e) {
				object = null;
			} catch (InvocationTargetException e) {
				object = null;
			}
    	}
    }

}
