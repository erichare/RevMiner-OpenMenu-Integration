<?php


# This allows signup.php to use functions in common.php.
include("common.php");
# call function top() for the header logo.
top();
# if it is normal GET request, then display a form for submitting.
if ($_SERVER["REQUEST_METHOD"] == "GET") {
	?>
	<form action="" method="post" enctype="multipart/form-data">
		<fieldset id="new_menu">
			<legend>Add Your Food:</legend>
			<div>
				<strong>Restaurant Name</strong>
				<input type="text" name="name" size="20"/>
			</div>
			
			<div>
				<strong>Food Name:</strong> 
				<input type="text" name="food" size="20"/>
			</div>
			
			<div>
				<strong>Food Description</strong>
				<input type="text" name="desc" size="20"/>
			</div>
			
			<div>
				<strong>Street Address1</strong>
				<input type="text" name="street1" size="20"/>
			</div>
			<div>
				<strong>Street Address2</strong>
				<input type="text" name="street2" size="20"/>
			</div>
			
			<div>
				<strong>City</strong>
				<input type="text" name="city" size="15"/>
			</div>
			
			<div>
				<strong>State</strong>
				<input type="text" name="state" size="10"/>
			</div>
			
			<div>
				<strong>Country</strong>
				<input type="text" name="country" size="13"/>
			</div>
			
			<div>
				<strong>Price</strong> 
				<input type="text" name="price1" size="3"/>
				to
				<input type="text" name="price2" size="3"/>
			</div>
			
			<div>
				<input type="submit" value="Sign Up" />			
			</div>
		</fieldset>
	</form>
<?php
# If it is a post request, then call a function getNewLine() 
# to read data from the query parameters,
# and then store it to $new_line_to_add.
} elseif($_SERVER["REQUEST_METHOD"] == "POST"){
	$new_line_to_add = getNewLine();
	?>
	<p>
		<strong>Thank you for adding <?= $_REQUEST["name"] ?>!</strong>
	</p>
	
	<p> Your Contribution will be valuable!</p>
	
	<p>
		Now <a href="type_menu.php">go and find your Favoriate Food!</a>
	</p>

	<?php
	# add menu to a file
	# will be using xml for milestone2.
	file_put_contents("food.txt", $new_line_to_add."\n", FILE_APPEND);
}

# add new line in the data file.
function getNewLine(){
	$new_line = array($_REQUEST["name"],$_REQUEST["food"],$_REQUEST["desc"],
			$_REQUEST["street1"],$_REQUEST["street2"],$_REQUEST["city"],
			$_REQUEST["state"],$_REQUEST["country"],$_REQUEST["price1"],$_REQUEST["price2"]);
	$newline = implode(",", $new_line);
	return $newline;

}

# bottom is for footer notes/images, and validation links to check.
bottom();
?>
