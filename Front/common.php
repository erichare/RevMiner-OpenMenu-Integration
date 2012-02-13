<?php
# contact: gajok@cs.washington.edu
# filename: common.php
function top(){
    ?>
<!DOCTYPE html>
<html lang="en">
<!--<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	Very First Page for Restaurant Searcher -->
	<head>
		<title>Menu by Menu</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="description" content="Restaurant Searcher" />
		<meta name="keywords" content="restaurant, menu, food, delicious, dinner, lunch" />

		<!-- stop the web browser from ever caching this page or its images 
		<meta http-equiv="Cache-Control" content="no-cache" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />-->

		<link href="index.css" type="text/css" rel="stylesheet" />
		<link href="http://students.washington.edu/dongm/454/img/fifteen.gif" type="image/gif" rel="shortcut icon" />

		<script src="http://ajax.googleapis.com/ajax/libs/prototype/1.6.1.0/prototype.js" type="text/javascript"></script>		
    <script src="http://ajax.googleapis.com/ajax/libs/scriptaculous/1.8.3/scriptaculous.js" type="text/javascript"></script>
		<script src="list.js" type="text/javascript"></script>
		<script src="http://maps.google.com/maps?file=api&v=1&key=ABQIAAAAha3tZBPtoRIZLy8L__KptBRew3tpyxyDwTGHF7Wnf1NbbYENLhRB4lh8T7WgtB3lrbar3qfS7JjuLA" type="text/javascript"></script>
	</head>

	<body>
		<div id="bannerarea">
			Sick Foods For Sick People
		</div>
<?php
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
		<a id="home" href="index.php"> <strong> HOME </strong> </a>
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
				<strong>Food/Restaurant Name:</strong>
				<input type="text" name="menu_rest_name" size="25"/>
				<input type="submit" value="Find Food!" />
			</div>
		</fieldset>
	</form>
</div>
<?php } 

function item_list(){
	?>
	<div class="user">
		<div>
			<!--<input id="todo" type="text" /> 
		 <button id="add">Add to Bottom</button> -->
			<button id="delete">Delete Top Item</button>
		</div>
		
		<fieldset id="output">
			<legend>Your Favoriate Restaurant/Menu</legend>
			<div id="list">
				<ol id="foods">
					<li id="hidden" class="busy"></li>
				</ol>
			</div>
		</fieldset>
	</div>
<?php } ?> 
