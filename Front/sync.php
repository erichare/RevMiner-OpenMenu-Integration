<?php

/*******************/
	$dbhost = 'localhost'; //The host server of the database
	$dbname = 'dawgsfor_openmenu'; //The name of the database to access
	$dbuser = 'dawgsfor_omuser'; //The user id for accessing the database
	$dbpassword = '454ftw'; //the password for the user specified in dbuser
	$db = mysql_connect($dbhost, $dbuser, $dbpassword);
	if (!$db) {
		die("Error, could not connect to server!" . mysql_error());
	}

	if (!mysql_select_db($dbname)) {
		die("Error, could not choose the db! " . mysql_error());
	}
/*****************/

$modified = "";
if (isset($_REQUEST["timestamp"])) {
	$modified = "WHERE modified > FROM_UNIXTIME($timestamp)";
}

// Call and print the function if all parameters are passed
echo (get_restaurants($modified));
echo (get_items($modified));
echo (get_restaurants_items($modified));

mysql_close($db);

function get_restaurants($modified) {

	$query = "SELECT * FROM restaurants $modified";

	$result = mysql_query($query);
	if (!$result) {
		echo mysql_error();
		die ("MySQL encountered an error: " + mysql_error());
	}

	$json_array = array();
	while ($row = mysql_fetch_object($result)) {
		$row_array = array( 
			'rid'   => (int)$row->rid,
			'name'  => (string)$row->name,
			'address' => (string)$row->address,
			'city' => (string)$row->city,
			'state' => (string)$row->state,
			'country' => (string)$row->country,
		);

		array_push($json_array, $row_array);    
	}

	return json_encode($json_array); 
}

function get_items($modified) {

	$query = "SELECT * FROM items $modified";

	$result = mysql_query($query);
	if (!$result) {
		echo mysql_error();
		die ("MySQL encountered an error: " + mysql_error());
	}

	$json_array = array();
	while ($row = mysql_fetch_object($result)) {
		$row_array = array( 
			'iid'   => (int)$row->iid,
			'name'  => (string)$row->name,
			'description' => (string)$row->description,
			'price' => (string)$row->price,
		);

		array_push($json_array, $row_array);    
	}

	return json_encode($json_array); 
}

function get_restaurants_items($modified) {

	$query = "SELECT * FROM restaurants_items $modified";

	$result = mysql_query($query);
	if (!$result) {
		echo mysql_error();
		die ("MySQL encountered an error: " + mysql_error());
	}

	$json_array = array();
	while ($row = mysql_fetch_object($result)) {
		$row_array = array( 
			'rid'   => (int)$row->rid,
			'iid'  => (int)$row->iid,
		);

		array_push($json_array, $row_array);    
	}

	return json_encode($json_array); 
}
