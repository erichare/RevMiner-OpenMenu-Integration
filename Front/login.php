<?php
# Donghyo Min
# contact: gajok@cs.washington.edu
# filename: login.php
# log in page

session_start();

#include("userDbConfig.php");

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


if ($_GET["op"] == "login"){
		# Check if the connection is made well; else, print error message.
	check(mysql_pconnect($host, $user, $pass), "mysql_connect");

	# Check if database choosing is made well; else, print error message.
	check(mysql_select_db($db), "mysql_select_db");
	
	if (!$_POST["username"] || !$_POST["password"]){
		die("You need to provide a username and password.");
	}

	$q = "SELECT * FROM `dawgsfor_users`.`user_info` "
        ."WHERE `uid`='".$_POST["username"]."' "
        ."AND `pw`= '".$_POST["password"]."' "
        ."LIMIT 1";
  # Do query
  $r = mysql_query($q);
  
  if ( $obj = mysql_fetch_object($r) ){
	#	echo "log in was successful";
		# Login was successful. 
		# create session variables
		$_SESSION["valid_id"] = $obj->id;
		$_SESSION["valid_user"] = $_POST["username"];
		$_SESSION["valid_time"] = time();

		# Redirect to another page
		header("Location: personal.php");
	} else {
		# Login was not successful
		die("Sorry, could not log you in. Wrong login information.");
	}
} else {
	include("common.php");
	top();
?>
	<h1> Please, log-in here </h1>
	<form action="?op=login" method="POST">
		Username: <input name="username" MAXLENGTH="16">
		Password: <input type="password" name="password" MAXLENGTH="16">
		<input type="submit" value="Login">
	</form>
	<?php	
	bottom();
} 
?>
