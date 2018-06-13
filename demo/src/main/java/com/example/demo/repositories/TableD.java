package com.example.demo.repositories;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TableD {
	private @Id @GeneratedValue Long id;
	Date ycPublicDate;
	Date shouShuDate;
	long shengChanZhouQi;
	String bookName;
	float price;
	int count;
	float maYang;
	String type;
	String businessType;

	public void setPrice(float price) {
		this.price = price;
	}

	public float getPrice() {
		return price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getYcPublicDate() {
		return ycPublicDate;
	}

	public void setYcPublicDate(Date ycPublicDate) {
		this.ycPublicDate = ycPublicDate;
	}

	public Date getShouShuDate() {
		return shouShuDate;
	}

	public void setShouShuDate(Date shouShuDate) {
		this.shouShuDate = shouShuDate;
	}

	public long getShengChanZhouQi() {
		return shengChanZhouQi;
	}

	public void setShengChanZhouQi(long shengChanZhouQi) {
		this.shengChanZhouQi = shengChanZhouQi;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
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
