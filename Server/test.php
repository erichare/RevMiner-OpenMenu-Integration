<?php

/* 
include "FINsert/config.php"; // get db credentials ...
include "FINsert/opendb.php"; // ... which are used to open the db
*/

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


$cat; $rid; $bid;

/**
 * $cat should be passed as a name-value pair using GET or POST. 
 * The value should be an array, condensed into the following form:
 * original array: [1,2,3,4,5]
 * $cat array    : "1 2 3 4 5"
 * Essentially, it is storing the array as a string
 */
$cat = html_entity_decode($_REQUEST["cat"]);
$cat_array = explode(" ", $cat);
$rid = html_entity_decode($_REQUEST["rid"]);
$bid = html_entity_decode($_REQUEST["bid"]);

// Call and print the function if all parameters are passed
if (isset($_REQUEST["cat"]) && isset($_REQUEST["rid"])) {
    echo (get_object($rid, $bid, $cat_array));
}

/**
 * get_single_category function accepts a single category and outputs
 * in JSON the items associated with a given category and rid
 * All variables except $cat are used in the same fashion as get_object.
 * @returns: JSON string
 */
function get_single_category($rid, $bid, $cat) {
	
	$singleBuilding = "";
	
	if ($bid > 0) {
		$floor = ", f.name AS floor";
		$building = "JOIN floors f ON i.fid = f.fid";
		$singleBuilding = "AND f.bid = '$bid'";
	}
	 
	$query = "SELECT *, i.item_id AS id $floor FROM items i "
	       . "JOIN categories c ON i.cat_id = c.cat_id "
	       . "$building "
	       . "WHERE c.name = '$cat' AND i.rid = '$rid' "
		   . "$singleBuilding LIMIT 0, 100";
		   	       
	$result = mysql_query($query);
	if (!$result) {
		echo mysql_error();
		die ("MySQL encountered an error: " + mysql_error());
	}
	
	$json_array = array();
	while ($row = mysql_fetch_object($result)) {
		$row_array = array( 
			'id'   => (int)$row->id,
			'lat'  => (int)$row->latitude,
			'long' => (int)$row->longitude,
			'info' => $row->special_info,
			'fid' => (int)$row->fid,
			'cat' => $cat,
		);
		// This is probably a good thing to have with an item.
		if ($building_id) {
	        	$row_array['bid'] = (int)$row->bid;
		}
		array_push($json_array, $row_array);    
	}
	
	return json_encode($json_array); 
	break; // probably unnecessary with return?
}

include "FINsert/closedb.php"; // close the db connection