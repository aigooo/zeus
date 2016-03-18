package com.zeus.persist;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import com.zeus.common.util.TypeUtil;
import com.zeus.persist.util.MapUtil;

/**
 * Service的模型操作实现
 */
public class PersistHelper<T,E> implements CRUD<T>,Aggregate<T>{

    private BaseDao<T,E> baseDao;

    @Override
    public T save(T t) {
        return baseDao.save(t);
    }

    @Override
    public int saveBatch(Collection<T> ts) {
        return baseDao.saveBatch(ts);
    }

    @Override
    public T update(T t) {
        return baseDao.update(t);
    }

    @Override
    public int updateBatch(Collection<T> ts) {
        return baseDao.updateBatch(ts);
    }

    @Override
    public T saveOrUpdate(T t) {
        return baseDao.saveOrUpdate(t);
    }

    @Override
    public int saveOrUpdateBatch(Collection<T> ts) {
        return baseDao.saveOrUpdateBatch(ts);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(T t) {
        try {
            Method method = t.getClass().getMethod("getId");
            E key = (E) method.invoke(t);
            if(key!=null){
                return baseDao.get((Class<T>) t.getClass(),key);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public T prevGet(T t) {
        return baseDao.prevGet(t);
    }

    @Override
    public T nextGet(T t) {
        return baseDao.nextGet(t);
    }

    @Override
    public Collection<T> find(T t) {
        return baseDao.getList(t);
    }

    @Override
    public Collection<T> findAll() {
        return baseDao.getList(null);
    }
    
    @Override
	public T findOne(T t) {
		List<T> ts = baseDao.getList(t,0,1);
		if(ts!=null&&!ts.isEmpty()){
			return ts.get(0);
		}else{
			return null;
		}
	}

    @Override
    public T remove(T t) {
        return baseDao.delete(t);
    }

    @Override
    public int removeBatch(Collection<T> ts) {
        return baseDao.deleteBatch(ts);
    }

    @Override
    public void findPage(T t, Page<T> page) {
        baseDao.getPage(page,t);
    }

    @Override
    public void truncate() {
        MapUtil.objectToTable(this.getClass());
    }

    @Override
    public void removeByCondition(T t) {
        baseDao.removeByCondition(t);
    }

    @Override
    public Number getAvg(String proName, T t, int begin, int length) {
        return baseDao.getAvg(proName, t, begin, length);
    }

    @Override
    public Number getMid(String proName, T t, int begin, int length) {
        return baseDao.getMid(proName, t, begin, length);
    }

    @Override
    public Number getMax(String proName, T t, int begin, int length) {
        return baseDao.getMax(proName, t, begin, length);
    }

    @Override
    public Number getMin(String proName, T t, int begin, int length) {
        return baseDao.getMin(proName, t,begin,length);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void setBaseDao(BaseDao baseDao){
        this.baseDao = baseDao;
    }

//    @Override
//    public GroupResult group(T t, List<String> groups, List<String> indicators) {
//        return baseDao.group(t,groups,indicators);
//    }

	@SuppressWarnings("unchecked")
	@Override
	public <F> List<F> getDistinctProValue(String proName, T t, int begin, int length, Class<F> clazz) {
		return baseDao.getDistinctProValue(proName, t, begin, length);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <F> List<F> getList4ProValue(String proName, T t, int begin, int length, Class<F> clazz) {
		return baseDao.getList4ProValue(proName,t,begin,length);
	}

	@Override
	public Object load(T t,String propName) throws Exception{
		t = get(t);
		Object obj = t.getClass().getMethod(TypeUtil.attributeToGetter(propName)).invoke(t);
		if(obj==null) return null;
		return baseDao.init(obj);
	}
}
