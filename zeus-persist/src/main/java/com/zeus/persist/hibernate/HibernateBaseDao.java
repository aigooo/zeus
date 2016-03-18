package com.zeus.persist.hibernate;

import java.util.List;

import com.zeus.persist.BaseDao;
import com.zeus.persist.model.IdModel;

public interface HibernateBaseDao<T extends IdModel<K>, K> extends BaseDao<T, K>{
	
	/**
	 * 执行HQL
	 * @param hql
	 * @return
	 */
	void hqlExecute(String hql,Object[] values);
	
	/**
	 * 执行查询HQL
	 * @param hql
	 * @return
	 */
	List<T> hqlList(String hql,Object[] values);
	
}
