package com.example.demo.repositories;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TableF1 {
	private @Id @GeneratedValue Long id;
	String type;
	int pinZhongCount;
	int count;
	float maYang;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public int getPinZhongCount() {
		return pinZhongCount;
	}
	public void setPinZhongCount(int pinZhongCount) {
		this.pinZhongCount = pinZhongCount;
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
