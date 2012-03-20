<?php

/*******************/
	$dbhost = 'localhost'; //The host server of the database
	$dbname = 'rafty_openmenu'; //The name of the database to access
	$dbuser = 'rafty_omuser'; //The user id for accessing the database
	$dbpassword = 'Op3nM3nu!'; //the password for the user specified in dbuser
	$db = mysql_connect($dbhost, $dbuser, $dbpassword);
	if (!$db) {
		die("Error, could not connect to server!" . mysql_error());
	}
		
	if (!mysql_select_db($dbname)) {
		die("Error, could not choose the db! " . mysql_error());
	}
/*****************/

// The file OpenMenu.xml contains an XML document with a root element
// and at least an element /[root]/title.

if (file_exists('OpenMenu.xml')) {
    $xml = simplexml_load_file('OpenMenu.xml');


	foreach($xml->results->item as $item) {		 
		$q = mysql_query("INSERT INTO restaurants (name, address, city, state, country, lat, lon) VALUES ('$item->restaurant_name', '$item->address_1', '$item->city_town', '$item->state_province', '$item->country', 0, 0)");
			
		if (!$q) {
			print("Error, could not query the db! " . mysql_error());
		} else {
			print("You've successfully inserted a restaurant");
		
			$xml2 = simplexml_load_file($item->omf_file_url);
			
			foreach($xml2->menus->menu->menu_groups->menu_group as $menu_group) {
				foreach($menu_group->menu_items->menu_item as $menu_item) {
					$price = $menu_item->menu_item_price;
					if ((string)$price == NULL)) {
						$price = 0.00;
					}

					$q = mysql_query("INSERT INTO items (name, description, price, veg) VALUES ('$menu_item->menu_item_name', '$menu_item->menu_item_description', $price, 0)");
				
					if (!$q) {
						print("Error, could not query the db! " . mysql_error());
					} else {
						print("You've successfully inserted an item");
						
						$q = mysql_query("SELECT rid FROM restaurants ORDER BY rid DESC LIMIT 1");			
						$row = mysql_fetch_array($q);
						$rid = $row["rid"];
						
						$q = mysql_query("SELECT iid FROM items ORDER BY iid DESC LIMIT 1");
						$row = mysql_fetch_array($q);
						$iid = $row["iid"];
						
						$q = mysql_query("INSERT INTO restaurants_items (rid, iid) VALUES ($rid, $iid)"); 
						if (!$q) {
							print("Error, could not query the db! " . mysql_error());
						} else {
							print("You've successfully inserted a menu item link");
						}
					}
				}
			}
		}
	}
    
    // SEND THIS INFORMATION TO THE DATABASE
} else {
    exit('Failed to open OpenMenu.xml.');
}
?>