<?php
# author: Donghyo Min

$db   = "dawgsfor_users";
$host = "localhost";
$user = "dawgsfor_omuser";
$pass = "454ftw";
session_start();
# check result is not false/null; 
# else prints error
function check($result, $message) {
   if (!$result) {
      die("SQL error during $message: " . mysql_error());
   }
}

if($_GET["op"] == "reg") {
		
	# Check if the connection is made well; else, print error message.
	check(mysql_pconnect($host, $user, $pass), "mysql_connect");

	# Check if database choosing is made well; else, print error message.
	check(mysql_select_db($db), "mysql_select_db");
	
	$bInputFlag = false;
  foreach ( $_POST as $field ){
		if ($field == ""){
			$bInputFlag = false;
		} else {
			$bInputFlag = true;
		}
	}

	if ($bInputFlag == false){
		die( "Problem with your registration info. Please go back and try again.");
	}

	$q = "INSERT INTO `dawgsfor_users`.`user_info` (`uid`,`pw`,`email`) "
					."VALUES ('".$_POST["username"]."', '".$_POST["password"]."', '".$_POST["email"]."')";
	//  Run query
	$r = mysql_query($q);

	// Error checking after inserting a new id.
  if (!$r){
		die("Error: User not added to database.");
	} else {
		Header("Location: register.php?op=thanks");
	}
} elseif($_GET["op"] == "thanks" ){
	#echo "<h2>Thanks for registering!</h2>";
	# Login was successful. 
		# create session variables
		$_SESSION["valid_id"] = $_POST["username"];
		$_SESSION["valid_user"] = $_POST["username"];
		$_SESSION["valid_time"] = time();

		# Redirect to another page
		Header("Location: personal.php");
} else {
?>
	<p> Join Us, and Get Personalized Service </p>
	<form action="?op=reg" method="POST">
		Username: <input name="username" MAXLENGTH="16"> <br />
		Password: <input type="password" name="password" MAXLENGTH="16"> <br />
		Email Address: <input name="email" MAXLENGTH="25">
		<input type="submit" value="Join Us">
	</form>
<?php
}?>
