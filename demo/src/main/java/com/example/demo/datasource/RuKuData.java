package com.example.demo.datasource;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties
@JsonPropertyOrder({ "收书日期", "印次出版日期", "库区", "书名", "印次", "单价", "实收数量", "码洋" })
public class RuKuData {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@JsonProperty("收书日期")
	Date shouShuDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@JsonProperty("印次出版日期")
	Date ycPublicDate;
	@JsonProperty("库区")
	String kuQu;
	@JsonProperty("书名")
	String bookName;
	@JsonProperty("印次")
	String yinCi;
	@JsonProperty("单价")
	float price;
	@JsonProperty("实收数量")
	int count;
	@JsonProperty("码洋")
	float maYang;

	public Date getShouShuDate() {
		return shouShuDate;
	}

	public void setShouShuDate(Date shouShuDate) {
		this.shouShuDate = shouShuDate;
	}

	public Date getYcPublicDate() {
		return ycPublicDate;
	}

	public void setYcPublicDate(Date ycPublicDate) {
		this.ycPublicDate = ycPublicDate;
	}

	public String getKuQu() {
		return kuQu;
	}

	public void setKuQu(String kuQu) {
		this.kuQu = kuQu;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getYinCi() {
		return yinCi;
	}

	public void setYinCi(String yinCi) {
		this.yinCi = yinCi;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public float getMaYang() {
		return maYang;
	}

	public void setMaYang(float maYang) {
		this.maYang = maYang;
	}

}
