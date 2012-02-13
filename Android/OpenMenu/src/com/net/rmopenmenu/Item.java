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
			if (this.item_price.equals("Unknown Price") || other.item_price.equals("Unknown Price")) {
				return this.item_price.compareTo(other.item_price);
			} else {
				double compare = (Double.valueOf(this.item_price) - Double.valueOf(other.item_price));
				if (compare < 0) {
					return -1;
				} else if (compare > 0) {
					return 1;
				} else {
					return 0;
				}
			}
		} else {
			return 0;
		}
	}
}
