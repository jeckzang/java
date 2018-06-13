package com.example.demo.datasource;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties
@JsonPropertyOrder({ "通知日期", "书名", "印次", "定价", "印单印数", "总码洋" })
public class RunTimeData {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@JsonProperty("通知日期")
	Date tongZhiRiQi;
	@JsonProperty("书名")
	String bookName;
	@JsonProperty("印次")
	String yinCi;
	@JsonProperty("定价")
	String price;
	@JsonProperty("印单印数")
	int yinDanCiShu;
	@JsonProperty("总码洋")
	float zongMaYang;

	public Date getTongZhiRiQi() {
		return tongZhiRiQi;
	}

	public void setTongZhiRiQi(Date tongZhiRiQi) {
		this.tongZhiRiQi = tongZhiRiQi;
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getYinDanCiShu() {
		return yinDanCiShu;
	}

	public void setYinDanCiShu(int yinDanCiShu) {
		this.yinDanCiShu = yinDanCiShu;
	}

	public float getZongMaYang() {
		return zongMaYang;
	}

	public void setZongMaYang(float zongMaYang) {
		this.zongMaYang = zongMaYang;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bookName == null) ? 0 : bookName.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((tongZhiRiQi == null) ? 0 : tongZhiRiQi.hashCode());
		result = prime * result + ((yinCi == null) ? 0 : yinCi.hashCode());
		result = prime * result + yinDanCiShu;
		result = prime * result + Float.floatToIntBits(zongMaYang);
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
		RunTimeData other = (RunTimeData) obj;
		if (bookName == null) {
			if (other.bookName != null)
				return false;
		} else if (!bookName.equals(other.bookName))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (tongZhiRiQi == null) {
			if (other.tongZhiRiQi != null)
				return false;
		} else if (!tongZhiRiQi.equals(other.tongZhiRiQi))
			return false;
		if (yinCi == null) {
			if (other.yinCi != null)
				return false;
		} else if (!yinCi.equals(other.yinCi))
			return false;
		if (yinDanCiShu != other.yinDanCiShu)
			return false;
		if (Float.floatToIntBits(zongMaYang) != Float.floatToIntBits(other.zongMaYang))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RuKuData [tongZhiRiQi=" + tongZhiRiQi + ", bookName=" + bookName + ", yinCi=" + yinCi + ", price="
				+ price + ", yinDanCiShu=" + yinDanCiShu + ", zongMaYang=" + zongMaYang + "]";
	}

}
