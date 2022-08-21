package com.example.demo;

import java.io.Serializable;

import lombok.Data;

@Data
public class Addressinfo implements Serializable {
    /**网点名称*/
    private String title;
    /**网点地址*/
    private String address;
    /**网点联系方式*/
    private String phone;
    /**网点坐标-精度*/
    private double x;
    /**网点坐标-纬度*/
    private double y;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
    
    
}
