<?php
#author: Donghyo Min
#filename: list_save.php
#
#This file is for php(web-service). 

# Define MIME
header("Content-type: text/plain");
session_start();
$list_food = "f_".$_SESSION["valid_user"].".txt";
$list_rest = "r_".$_SESSION["valid_user"].".txt";

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
			if(file_exists($list_food)){
				contents($list_food);
			}
		} else if ($behavior == "get_rest"){
			# process a GET request
			if(file_exists($list_rest)){
				contents($list_rest);
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
				file_put_contents($list_food, $newLine, FILE_APPEND);
			//	contents("list_food.txt");
			}
		
		# For adding element. 
		# Checking parameter "action"'s value is "add_rest".
		# Then, add the new "item" to the end of the file.
		} else if ($behavior == "add_rest"){
			if (isset($_REQUEST["item"])) {
				$newLine = $_REQUEST["item"]."\n";
				file_put_contents($list_rest, $newLine, FILE_APPEND);
			//	contents("list_rest.txt");
			}
			
		# for delete operation, 
		# Delete the first line, and write the rest to the file.
		# To do it, get the all the lines but the first line.
		# Then, make a new string, and then write it on the file.	
		}	else if ($behavior == "delete_food"){
			if(file_exists($list_food)){
				$updated = "";		
				$lines = file_get_contents($list_food); 
				$tokens = explode("\n", $lines);
				$i = 0;
				for($i = 1; $i < count($tokens) - 1; $i++){
					$updated = $updated.$tokens[$i]."\n";	
				}
				$updated = $updated.$tokens[$i];
				file_put_contents($list_food, $updated);
			//	contents("list_food.txt");
			}
		} else if ($behavior == "delete_rest"){
			if(file_exists($list_rest)){
				$updated = "";		
				$lines = file_get_contents($list_rest); 
				$tokens = explode("\n", $lines);
				$i = 0;
				for($i = 1; $i < count($tokens) - 1; $i++){
					$updated = $updated.$tokens[$i]."\n";	
				}
				$updated = $updated.$tokens[$i];
				file_put_contents($list_rest, $updated);
			//	contents("list_rest.txt");
			}
			
		# When the list's order changed, this plays a role.
		# This part is going to get the new string after switching,
		# then write it on the file. 
		} else if($behavior == "set_food"){
			if (isset($_REQUEST["items"])) {
				$newSet = $_REQUEST["items"];
				file_put_contents($list_food, $newSet);
			//	contents("list_food.txt");
			}
		} else if($behavior == "set_rest"){
			if (isset($_REQUEST["items"])) {
				$newSet = $_REQUEST["items"];
				file_put_contents($list_rest, $newSet);
			//	contents("list_rest.txt");
			}
		} else if($behavior == "delete_any_rest"){
			if (isset($_REQUEST["name_to_del"])) {
				$deleting = $_REQUEST["name_to_del"];
				if(file_exists($list_rest)){
					$updated = "";		
					$lines = file_get_contents($list_rest); 
					$tokens = explode("\n", $lines);
					$i = 0;
					for($i = 0; $i < count($tokens) - 1; $i++){
						if($tokens[$i] != $deleting){
							$updated = $updated.$tokens[$i]."\n";	
						}
					}
					if($tokens[$i] != $deleting){
						$updated = $updated.$tokens[$i];
					}
					file_put_contents($list_rest, $updated);
				//	contents("list_rest.txt");
				}
			}
		} else if($behavior == "delete_any_food"){
			if (isset($_REQUEST["name_to_del"])) {
				$deleting = $_REQUEST["name_to_del"];
				if(file_exists($list_food)){
					$updated = "";		
					$lines = file_get_contents($list_food); 
					$tokens = explode("\n", $lines);
					$i = 0;
					for($i = 0; $i < count($tokens) - 1; $i++){
						if($tokens[$i] != $deleting){
							$updated = $updated.$tokens[$i]."\n";	
						}
					}
					if($tokens[$i] != $deleting){
						$updated = $updated.$tokens[$i];
					}
					file_put_contents($list_food, $updated);
				//	contents("list_rest.txt");
				}
			}
		}
	}	 	
}
?>
