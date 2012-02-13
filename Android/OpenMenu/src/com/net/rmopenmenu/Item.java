package com.net.rmopenmenu;

public class Item implements Comparable<Item> {
	int item_id;
	String restaurant_name;
	String restaurant_address;
	String item_name;
	String item_price;
	String item_description;
	
	private boolean sortPrice;
	
	public Item(int item_id, String restaurant_name, String restaurant_address, String item_name, String item_price, String item_description, boolean sortPrice) {
		this.item_id = item_id;
		this.restaurant_name = restaurant_name;
		this.restaurant_address = restaurant_address;
		this.item_name = item_name;
		this.item_price = item_price;
		this.item_description = item_description;
		
		this.sortPrice = sortPrice;
	}

	@Override
	public int compareTo(Item other) {
		if (sortPrice) {
			return this.item_price.compareTo(other.item_price);
		} else {
			return 0;
		}
	}
}
