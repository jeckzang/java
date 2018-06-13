package com.example.demo.datasource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties
@JsonPropertyOrder({ "书名", "类型", "业务分类" })
public class BookAndTypeData {
	@JsonProperty("书名")
	String bookName;
	@JsonProperty("类型")
	String type;
	@JsonProperty("业务分类")
	String businessType;

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

}
