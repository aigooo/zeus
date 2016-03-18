package com.zeus.persist;

import java.util.Collection;

/**
 * Service
 */
public interface CRUD<T> {
	
	Object load(T t,String propName) throws Exception;

    T save(T t);

    int saveBatch(Collection<T> ts);

    T update(T t);

    int updateBatch(Collection<T> ts);

    T saveOrUpdate(T t);

    int saveOrUpdateBatch(Collection<T> ts);

    T get(T t);

    T prevGet(T t);

    T nextGet(T t);

    Collection<T> find(T t);

    Collection<T> findAll();
    
    T findOne(T t);

    T remove(T t);

    int removeBatch(Collection<T> ts);

    void findPage(T t,Page<T> page);

    void truncate();

    void removeByCondition(T t);
}
