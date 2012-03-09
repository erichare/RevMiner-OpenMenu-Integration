<?php
# contact: gajok@cs.washington.edu
# filename: common.php
session_start();

function top(){
    ?>
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Menu by Menu</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="description" content="Restaurant Searcher" />
		<meta name="keywords" content="restaurant, menu, food, delicious, dinner, lunch" />

		<!-- stop the web browser from ever caching this page or its images
		<meta http-equiv="Cache-Control" content="no-cache" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" /> -->

		<link href="index.css" type="text/css" rel="stylesheet" />
		<link href="http://students.washington.edu/dongm/454/img/fifteen.gif" type="image/gif" rel="shortcut icon" />

		<script src="http://ajax.googleapis.com/ajax/libs/prototype/1.6.1.0/prototype.js" type="text/javascript"></script>		
    <script src="http://ajax.googleapis.com/ajax/libs/scriptaculous/1.8.3/scriptaculous.js" type="text/javascript"></script>
		<script src="list.js" type="text/javascript"></script>
		<script src="http://maps.google.com/maps?file=api&v=1&key=ABQIAAAAha3tZBPtoRIZLy8L__KptBRew3tpyxyDwTGHF7Wnf1NbbYENLhRB4lh8T7WgtB3lrbar3qfS7JjuLA" type="text/javascript"></script>
	</head>

	<body>
		<div id="bannerarea">
			<strong>Sick Foods For Sick People</strong>
		</div>
		<?php
		if ($_SESSION["valid_user"]) {
		?>
			<div id="signup">
				<a href="logout.php"> logout!</a>
			</div>
		<?php			
		} else {
		?>	
			<div id="signup">
				<a href="login.php"> login </a> <br />
				New Here? <a href="register.php"> Sign Up </a>
			</div>
		<?php	
		}
}

# This function is for checking validation and copyright.
function bottom(){
    ?>      
    <div id="copyright">
			<p>
				Results and page (C) Copyright 2012 SFSP.
			</p>
		</div>
	</body>
</html>
<?php 
} 

function type_menu(){
		?>
<div id="searchbox">
	<div id="home_table">
		<a id="home" href="personal.php"> <strong> HOME </strong> </a>
	</div>
	<form id="turnin" action="match.php" method="get" enctype="multipart/form-data">	
		<fieldset>
			<legend>Type Menu/Restaurant You Want</legend>
			<div>
				<strong>Search</strong> 
				<label><input type="radio" name="want" value="Food" checked="checked"/> Food</label>
				<label><input type="radio" name="want" value="Restaurant"/> Restaurant</label>
			</div>
			<div>
				<strong>Food/Restaurant</strong>
				<input type="text" name="menu_rest_name" size="25"/>
				<input type="submit" value="Find Food!" />
			</div>
		</fieldset>
	</form>
</div>
<?php } 

function item_list(){
	?>
	<div class="user_item">
		<div class = "button_set">
			<input id="new_rest" type="text" /> 
			<button id="add_new_rest">Add to Bottom</button>
			<button id="delete_rest">Delete Top Item</button>
			<button id="deleteAll_rest">Delete All Item</button>
		</div>
		
		<fieldset id="output2">
			<legend>Your Favoriate Restaurants</legend>
			<div id="list2">
				<div class="reorder">You can drag and drop an item to reorder the list</div>
				<ol id="rests">
					<li id="hidden2" class="busy2"></li>
				</ol>
			</div>
		</fieldset>
	</div>
	
	<div class="user_item">
		<div class = "button_set">
			<input id="new_food" type="text" /> 
			<button id="add_new_food">Add to Bottom</button>
			<button id="delete_food">Delete Top Item</button>
			<button id="deleteAll_food">Delete All Item</button>
		</div>
		
		<fieldset id="output1">
			<legend>Your Favoriate Foods</legend>
			<div id="list1">
				<div class="reorder">You can drag and drop an item to reorder the list</div>
				<ol id="foods">
					<li id="hidden1" class="busy1"></li>
				</ol>
			</div>
		</fieldset>
	</div>
<?php } ?> 
