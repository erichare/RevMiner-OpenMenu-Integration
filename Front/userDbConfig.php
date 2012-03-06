<?php
# Donghyo Min
# contact: gajok@cs.washington.edu
# filename: userDbConfig.php
# This file is used to connect to DB for user info

$db   = "dawgsfor_users";
$host = "localhost";
$user = "dawgsfor_omuser";
$pass = "454ftw";

# check result is not false/null; 
# else prints error
function check($result, $message) {
   if (!$result) {
      die("SQL error during $message: " . mysql_error());
   }
}

# Check if the connection is made well; else, print error message.
check(mysql_pconnect($host, $user, $pass), "mysql_connect");

# Check if database choosing is made well; else, print error message.
check(mysql_select_db($db), "mysql_select_db");
?>
