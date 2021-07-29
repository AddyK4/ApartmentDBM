package com.skillstorm.beans;

public class Apartment {
	
	private int id;
	private String name;
	private String address;
	private int price;
	
	
	public Apartment() {
		
	}
	public Apartment(String name, String address, int price) {
		super();
		this.name = name;
		this.address = address;
		this.price = price;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "Apartment [id=" + id + ", name=" + name + ", address=" + address + ", price=" + price + "]";
	}
}
