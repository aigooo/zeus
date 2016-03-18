package com.zeus.persist;

import java.util.List;

public class Page<T> {
	
	private Integer count;
	private List<T> list;
	private int pageSize = 12;
	private int pageCount = 1;
	private int pageNbr = 1;
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getPageNbr() {
		return pageNbr;
	}
	public void setPageNbr(int pageNbr) {
		this.pageNbr = pageNbr;
	}
	public Boolean hasNextPage(){
		return pageNbr<pageCount;
	}
	
	public Boolean hasPrePage(){
		return pageNbr>1;
	}
	public int getPrePage() {
		return pageNbr-1;
	}
	public int getNextPage() {
		return pageNbr+1;
	}
	
	
}
