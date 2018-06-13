package com.example.demo.repositories;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TableB {
	private @Id @GeneratedValue Long id;
	String bookName;
	String DateRange;
	String price;
	int count;
	float maYang;
	String type;
	String businessType;
	int countFor90;
	int countFor180;
	int countFor360;
	String increase;

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getDateRange() {
		return DateRange;
	}

	public void setDateRange(String dateRange) {
		DateRange = dateRange;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
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

	public int getCountFor90() {
		return countFor90;
	}

	public void setCountFor90(int countFor90) {
		this.countFor90 = countFor90;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((DateRange == null) ? 0 : DateRange.hashCode());
		result = prime * result + ((bookName == null) ? 0 : bookName.hashCode());
		result = prime * result + ((businessType == null) ? 0 : businessType.hashCode());
		result = prime * result + count;
		result = prime * result + countFor180;
		result = prime * result + countFor360;
		result = prime * result + countFor90;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((increase == null) ? 0 : increase.hashCode());
		result = prime * result + Float.floatToIntBits(maYang);
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		TableB other = (TableB) obj;
		if (DateRange == null) {
			if (other.DateRange != null)
				return false;
		} else if (!DateRange.equals(other.DateRange))
			return false;
		if (bookName == null) {
			if (other.bookName != null)
				return false;
		} else if (!bookName.equals(other.bookName))
			return false;
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
		if (countFor90 != other.countFor90)
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
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TableB [bookName=" + bookName + ", DateRange=" + DateRange + ", price=" + price + ", count=" + count
				+ ", maYang=" + maYang + ", type=" + type + ", businessType=" + businessType + ", countFor90="
				+ countFor90 + ", countFor180=" + countFor180 + ", countFor360=" + countFor360 + ", increase="
				+ increase + "]";
	}

}
