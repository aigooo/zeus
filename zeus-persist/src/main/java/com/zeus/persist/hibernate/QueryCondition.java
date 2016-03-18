package com.zeus.persist.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.Type;

public class QueryCondition {
	
	Logger logger = Logger.getLogger(QueryCondition.class);
	
	private StringBuilder sql;
	
	private List<Object> params = new ArrayList<>();
	
	private List<Type> types = new ArrayList<>();
	
	public QueryCondition(){
		
	}
	
	public Query query(Session session){
		Query query = session.createQuery(this.toString());
		for(int i=0;i<params.size();i++){
			query.setParameter(i, params.get(i));
		}
		return query;
	}
	
	public Query sqlQuery(Session session){
		Query query = session.createSQLQuery(this.toString());
		for(int i=0;i<params.size();i++){
			query.setParameter(i, params.get(i));
		}
		return query;
	}
	
	public QueryCondition(String sql){
		this.sql = new StringBuilder(sql);
	}
	
	public QueryCondition append(Object obj){
		sql.append(obj);
		return this;
	}
	
	public QueryCondition prepend(Object obj){
		sql.insert(0, obj);
		return this;
	}
	
	public QueryCondition param(Object obj){
		params.add(obj);
		return this;
	}
	
	public QueryCondition params(List<Object> params){
		this.params.addAll(params);
		return this;
	}
	
	public StringBuilder getSql() {
		return sql;
	}

	public QueryCondition setSql(StringBuilder sql) {
		this.sql = sql;
		return this;
	}

	public Object[] getParams() {
		return params.toArray();
	}

	public Type[] getTypes() {
		return (Type[]) types.toArray();
	}

	@Override
	public String toString() {
		return sql.toString();
	}
}
