<?php
#author: Donghyo Min
#filename: list_save.php
#
#This file is for php(web-service). 

# Define MIME
header("Content-type: text/plain");

# As a web-service, this function is used to print the list's contents.
function contents($file_name){
	$lines = file_get_contents($file_name, FILE_IGNORE_NEW_LINES);
	print($lines);
}

# If the request is get, simply output the to-do list's content.
if ($_SERVER["REQUEST_METHOD"] == "GET") {
	# If there is an "action" value, then process it.
	if (isset($_REQUEST["action"])) {
		$behavior = $_REQUEST["action"];
		if($behavior == "get_food"){
			# process a GET request
			if(file_exists("list_food.txt")){
				contents("list_food.txt");
			}
		} else if ($behavior == "get_rest"){
			# process a GET request
			if(file_exists("list_rest.txt")){
				contents("list_rest.txt");
			}
		}
	}
# process a POST request  
} else if ($_SERVER["REQUEST_METHOD"] == "POST") {
	
	# If there is an "action" value, then process it.
	if (isset($_REQUEST["action"])) {
		$behavior = $_REQUEST["action"];
		
		# For adding element. 
		# Checking parameter "action"'s value is "add_food".
		# Then, add the new "item" to the end of the file.
		if($behavior == "add_food"){
			if (isset($_REQUEST["item"])) {
				$newLine = $_REQUEST["item"]."\n";
				file_put_contents("list_food.txt", $newLine, FILE_APPEND);
			//	contents("list_food.txt");
			}
		
		# For adding element. 
		# Checking parameter "action"'s value is "add_rest".
		# Then, add the new "item" to the end of the file.
		} else if ($behavior == "add_rest"){
			if (isset($_REQUEST["item"])) {
				$newLine = $_REQUEST["item"]."\n";
				file_put_contents("list_rest.txt", $newLine, FILE_APPEND);
			//	contents("list_rest.txt");
			}
			
		# for delete operation, 
		# Delete the first line, and write the rest to the file.
		# To do it, get the all the lines but the first line.
		# Then, make a new string, and then write it on the file.	
		}	else if ($behavior == "delete_food"){
			if(file_exists("list_food.txt")){
				$updated = "";		
				$lines = file_get_contents("list_food.txt"); 
				$tokens = explode("\n", $lines);
				$i = 0;
				for($i = 1; $i < count($tokens) - 1; $i++){
					$updated = $updated.$tokens[$i]."\n";	
				}
				$updated = $updated.$tokens[$i];
				file_put_contents("list_food.txt", $updated);
			//	contents("list_food.txt");
			}
		} else if ($behavior == "delete_rest"){
			if(file_exists("list_rest.txt")){
				$updated = "";		
				$lines = file_get_contents("list_rest.txt"); 
				$tokens = explode("\n", $lines);
				$i = 0;
				for($i = 1; $i < count($tokens) - 1; $i++){
					$updated = $updated.$tokens[$i]."\n";	
				}
				$updated = $updated.$tokens[$i];
				file_put_contents("list_rest.txt", $updated);
			//	contents("list_rest.txt");
			}
			
		# When the list's order changed, this plays a role.
		# This part is going to get the new string after switching,
		# then write it on the file. 
		} else if($behavior == "set_food"){
			if (isset($_REQUEST["items"])) {
				$newSet = $_REQUEST["items"];
				file_put_contents("list_food.txt", $newSet);
			//	contents("list_food.txt");
			}
		} else if($behavior == "set_rest"){
			if (isset($_REQUEST["items"])) {
				$newSet = $_REQUEST["items"];
				file_put_contents("list_rest.txt", $newSet);
			//	contents("list_rest.txt");
			}
		}
	}	 	
}
?>
