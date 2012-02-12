<?php
#author: Donghyo Min
#filename: list_save.php
#
#This file is for php(web-service). 

# Define MIME
header("Content-type: text/plain");

# As a web-service, this function is used to print the list's contents.
function contents(){
	$lines = file_get_contents("list.txt", FILE_IGNORE_NEW_LINES);
	print($lines);
}

# If the request is get, simply output the to-do list's content.
if ($_SERVER["REQUEST_METHOD"] == "GET") {
	# process a GET request
	if(file_exists("list.txt")){
		contents();
	}

# process a POST request  
} else if ($_SERVER["REQUEST_METHOD"] == "POST") {
	
	# If there is an "action" value, then process it.
	if (isset($_REQUEST["action"])) {
		$behavior = $_REQUEST["action"];
		
		# For adding element. 
		# Checking parameter "action"'s value is "add".
		# Then, add the new "item" to the end of the file.
		if($behavior == "add"){
			if (isset($_REQUEST["item"])) {
				$newLine = $_REQUEST["item"]."\n";
				file_put_contents("list.txt", $newLine, FILE_APPEND);
				contents();
			}
		
		# for delete operation, 
		# Delete the first line, and write the rest to the file.
		# To do it, get the all the lines but the first line.
		# Then, make a new string, and then write it on the file.
		} else if ($behavior == "delete"){
			if(file_exists("list.txt")){
				$updated = "";		
				$lines = file_get_contents("list.txt"); 
				$tokens = explode("\n", $lines);
				$i = 0;
				for($i = 1; $i < count($tokens); $i++){
					$updated = $updated.$tokens[$i]."\n";	
				}
				file_put_contents("list.txt", $updated);
				contents();
			}
			
		# When the list's order changed, this plays a role.
		# This part is going to get the new string after switching,
		# then write it on the file. 
		} else if($behavior == "set"){
			if (isset($_REQUEST["items"])) {
				$newSet = $_REQUEST["items"];
				file_put_contents("list.txt", $newSet);
				contents();
			}
		}
	}	 	
}
?>
