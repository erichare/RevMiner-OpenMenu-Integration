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

// The file OpenMenu.xml contains an XML document with a root element
// and at least an element /[root]/title.

if (file_exists('OpenMenu.xml')) {
    $xml = simplexml_load_file('OpenMenu.xml');
 
    print_r($xml);
} else {
    exit('Failed to open OpenMenu.xml.');
}
?>