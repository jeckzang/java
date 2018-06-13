package com.example.demo.restapis;

import java.util.List;

public class Pagination<T> {
	long totalCounts;
	long totalPages;
	List<T> dataList;

	public long getTotalCounts() {
		return totalCounts;
	}

	public void setTotalCounts(long totalCounts) {
		this.totalCounts = totalCounts;
	}

	public long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public List<T> getDataList() {
		return dataList;
	}

}
