package com.example.demo.repositories;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TableC2 {
	private @Id @GeneratedValue Long id;
	String businessType;
	int count;
	float maYang;
	int countFor180;
	int countFor360;
	String increase;
	String proportion;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getBusinessType() {
		return businessType;
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

	public int getCountFor180() {
		return countFor180;
	}

	public void setCountFor180(int countFor180) {
		this.countFor180 = countFor180;
	}

	public int getCountFor360() {
		return countFor360;
	}

	public void setCountFor360(int countFor360) {
		this.countFor360 = countFor360;
	}

	public String getIncrease() {
		return increase;
	}

	public void setIncrease(String increase) {
		this.increase = increase;
	}

	public String getProportion() {
		return proportion;
	}

	public void setProportion(String proportion) {
		this.proportion = proportion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((businessType == null) ? 0 : businessType.hashCode());
		result = prime * result + count;
		result = prime * result + countFor180;
		result = prime * result + countFor360;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((increase == null) ? 0 : increase.hashCode());
		result = prime * result + Float.floatToIntBits(maYang);
		result = prime * result + ((proportion == null) ? 0 : proportion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableC2 other = (TableC2) obj;
		if (businessType == null) {
			if (other.businessType != null)
				return false;
		} else if (!businessType.equals(other.businessType))
			return false;
		if (count != other.count)
			return false;
		if (countFor180 != other.countFor180)
			return false;
		if (countFor360 != other.countFor360)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (increase == null) {
			if (other.increase != null)
				return false;
		} else if (!increase.equals(other.increase))
			return false;
		if (Float.floatToIntBits(maYang) != Float.floatToIntBits(other.maYang))
			return false;
		if (proportion == null) {
			if (other.proportion != null)
				return false;
		} else if (!proportion.equals(other.proportion))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TableC2 [id=" + id + ", businessType=" + businessType + ", count=" + count + ", maYang=" + maYang
				+ ", countFor180=" + countFor180 + ", countFor360=" + countFor360 + ", increase=" + increase
				+ ", proportion=" + proportion + "]";
	}

}
