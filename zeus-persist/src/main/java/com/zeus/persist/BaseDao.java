package com.zeus.persist;

import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import com.zeus.persist.hibernate.QueryCondition;

public interface BaseDao<T,K> {

	/**
	 * 根据主键和类型获取实体对象
	 * @param clazz
	 * @param key
	 * @return
	 */
	T get(Class<T> clazz, K key);
	
	/**
	 * 取该对象的前一条数据
	 * @param t
	 * @return
	 */
	T prevGet(T t);
	
	/**
	 * 取该对象的下一条对象
	 * @param t
	 * @return
	 */
	T nextGet(T t);
	
	/**
	 * 保存实体对象
	 * @param t
	 */
	T save(T t);
	
	/**
	 * 批量保存实体对象
	 * @param ts
	 */
	int saveBatch(Collection<T> ts);
	
	/**
	 * 更新实体对象
	 * @param t
	 */
	T update(T t);

	/**
	 * 批量更新
	 * @param ts
	 * @return
	 */
	int updateBatch(Collection<T> ts);
	
	/**
	 * 保存或跟新实体对象
	 * @param t
	 */
	T saveOrUpdate(T t);
	
	/**
	 * 批量保存或跟新实体对象
	 * @param ts
	 */
	int saveOrUpdateBatch(Collection<T> ts);
	
	/**
	 * 删除实体对象
	 * @param t
	 */
	T delete(T t);


	/**
	 * 批量删除
	 * @param ts
	 * @return
	 */
	int deleteBatch(Collection<T> ts);
	
	/**
	 * 通过条件删除数据
	 * @param t
	 */
	void removeByCondition(T t);
	
	/**
	 * 分页获取实体对象
	 * @param tp
	 * @param t
	 */
	void getPage(Page<T> tp, T t);
	
	/**
	 * 按条件获取实体对象的条数
	 * @param t
	 * @return
	 */
	Integer getCount(T t);
	
	/**
	 * 根据条件获取实体对象
	 * @param t
	 * @return
	 */
	List<T> getList(T t);
	
	/**
	 * 根据条件获取实体对象，返回从begin（包括）开始，length条实体对象
	 * @param t
	 * @param begin 开始位置
	 * @param length  获取条数
	 * @return
	 */
	List<T> getList(T t, int begin, int length);
	
	/**
	 * 根据HQL获取实体对象，返回从begin（包括）当前，length条实体对象
	 * @param sql  执行的sql语句
	 * @param params 查询参数
	 * @param begin 开始位置
	 * @param length  获取条数
	 * @return
	 */
	List<T> getList(String sql,Object[] params, int begin, int length);
	
	/**
	 * 根据HQL获取实体对象，返回从begin（包括）当前，length条实体对象
	 * @param sql  执行的sql语句
	 * @param t 查询参数实体
	 * @param begin 开始位置
	 * @param length  获取条数
	 * @return
	 */
	List<T> getList(String sql,T t, int begin, int length);
	
	/**
	 * 根据条件（实体对象），生成SQL语句
	 * @param t
	 * @return 带?的sql语句
	 */
	QueryCondition createQueryCondition(T t);
	
	
	/**
	 * 执行SQL
	 * @param sql
	 */
	int sqlExecute(String sql);
	
	/**
	 * 执行查询SQL
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List sqlList(String sql);
	
	/**
	 * 执行查询SQL
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List sqlList(String sql, Object[] params);
	
	/**
	 * 合并实体对象
	 * @param t
	 */
	void merge(T t);
	
	/**
	 * 统计清单某属性的平均
	 * @param proName
	 * @param t
	 * @param begin
	 * @param length
	 * @return
	 * @addBy 夏植胜
	 * @addTime 2014-11-10 下午4:22:14
	 */
	Number getAvg(String proName, T t, int begin, int length);
	
	/**
	 * 统计清单某属性的中值
	 * @param proName
	 * @param t
	 * @param begin
	 * @param length
	 * @return
	 * @addBy 夏植胜
	 * @addTime 2014-11-10 下午4:22:14
	 */
	Number getMid(String proName, T t, int begin, int length);
	
	/**
	 * 统计清单某属性的最大值
	 * @param proName
	 * @param t
	 * @param begin
	 * @param length
	 * @return
	 * @addBy 夏植胜
	 * @addTime 2014-11-10 下午4:22:14
	 */
	Number getMax(String proName, T t, int begin, int length);
	
	/**
	 * 统计清单某属性的最小值
	 * @param proName
	 * @param t
	 * @param begin
	 * @param length
	 * @return
	 * @addBy 夏植胜
	 * @addTime 2014-11-10 下午4:22:14
	 */
	Number getMin(String proName, T t, int begin, int length);
	
	/**
	 * 获取某个属性的DistinctValue
	 * @param proName
	 * @param t
	 * @param begin
	 * @param length
	 * @return
	 * @addBy 夏植胜
	 * @addTime 2014-11-10 下午4:49:57
	 */
	@SuppressWarnings("rawtypes")
	List getDistinctProValue(String proName, T t, int begin, int length);

	/**
	 * 获取某个属性的Value
	 * @param proName  proName
	 * @param t  t
	 * @param begin begin
	 * @param length length
	 * @return list
	 * @addBy 夏植胜
	 * @addTime 2014-11-10 下午4:49:57
	 */
	@SuppressWarnings("rawtypes")
	List getList4ProValue(String proName, T t, int begin, int length);

	/**
	 * 设置DAO的数据源
	 * @param dateSource 数据源
	 */
	void setDateSource(DataSource dateSource) throws Exception;
	
	public Object init(Object object);
}
