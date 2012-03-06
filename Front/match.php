<?php
# contact: gajok@cs.washington.edu
# filename: match.php

#include common.php for using top() and bottom() function.
include("common.php");
session_start();
# This function is for <head> part.
top();

# check result is not false/null; 
# else prints error
function check($result, $message) {
   if (!$result) {
      die("SQL error during $message: " . mysql_error());
   }
}

# performs the given query
# If the result is false/null, print error.
# returns the result set 
function do_query($query) {
   # search database for the biggest cities
   $results = mysql_query($query);
   check($results, "mysql_query(\"$query\")");
   return $results;
}

function get_distance($lat, $lon){
	
	
}


# It gets Latitude and Longitude according to the address
function get_lat_lon($row_sub) {
	$q1 = str_replace(" ", "+", $row_sub[1]);
	$q2 = str_replace(" ", "+", $row_sub[2]);
	$q3 = str_replace(" ", "+", $row_sub[3]);
	$q4 = str_replace(" ", "+", $row_sub[4]);
	$q5 = str_replace(" ", "+", $row_sub[5]);
	$geocode=file_get_contents("http://maps.google.com/maps/api/geocode/json?address=$q1+$q2+$q3+$q4+$q5&sensor=false");
	$output= json_decode($geocode);
	$lat = $output->results[0]->geometry->location->lat;
	$long = $output->results[0]->geometry->location->lng;
	$arr = array("lat" => $lat, "lon" => $long);
	return $arr;
}

function table_head_for_food($caption){
?>
	<div id="table_start">
		<div id="capture">
			<?= $caption ?>
		</div>
		<table>
				<tr>
						<th>No.</th>
						<th>Restaurant Name</th>
						<th id="menu_name">menu name</th>
						<th>Add to My Menu</th>
						<th>Map</th>
						<th>Distance(mi)</th>
				</tr>
<?php 
}

function table_head_for_restaurant($caption){
?>
	<div id="table_start">
		<div id="capture">
			<div class="cap"><?= $caption ?></div>
		</div>
		<table>
				<tr>
						<th>No.</th>
						<th id="menu_name">menu name</th>
						<th>Discription</th>
						<th>Price</th>
						<th>Add to My Menu</th>
				</tr>
<?php
}

#It asks to do query. Once it gets the result, it will draw a table 
# to show matching restaurant.	  	
# For now, distance set to 0.
function menu_table($query_data, $food_name, $caption, $want){
    $result = do_query($query_data);
    $row = mysql_fetch_array($result);
    $location = get_lat_lon($row);
    if(!$row){
        ?>
        <p id="explain">
            OOOOPS. <strong>"<?= $food_name ?>"</strong> isn't in the database.<br />
            Try another food/restaurant.
        </p>
        <?php
    } else {    
				if($want == "Restaurant"){
				?>
					<div id="rest_info">
						<p> address: <?= $row[2] ?> <?= $row[3] ?> <?= $row[4] ?> <?= $row[5] ?></p>
						<p>
							<a href="map.php?lat=<?= $row[8] ?>&long=<?= $row[9] ?>" >
								<img src="http://www.project-fin.org/openmenu/Front/img/map_icon.jpg" alt="map_icon" />
								See On a Map
							</a>					
						</p>
						<p>
							<button id="fav_rest" value="<?=($row[1])?>">Add This Restaurant</button>
							<script type="text/javascript">
								var adding = document.getElementById("fav_rest");
								adding.observe("click", add_rest);
							</script>
						</p>
					</div>
				<?php
					table_head_for_restaurant($caption);
			
					$i = 1;
					while ($row) {
						if($i % 2 == 0){
								$zebra = "odd";
						} else {
								$zebra = "even";
						}
						?>
						
						<tr class = "<?= $zebra ?>" >
							<td> <?= $i ?> </td>
							<td> <?= htmlentities($row[0]) ?> </td>
							<td> <?= htmlentities($row[6]) ?> </td>
							<td> <?= htmlentities($row[7]) ?> </td>
							<td>
								<button id="<?=$i?>" value="<?=($row[0])?>">Add</button>
								<script type="text/javascript">
									var adding = document.getElementById("<?=$i?>");
									adding.observe("click", add_food);
								</script>
							</td>
						</tr>
							
						<?php
						$i++;
						$row = mysql_fetch_array($result);
					}
				} else if ($want == "Food"){
					table_head_for_food($caption);
					
					$i = 1;
					while ($row) {
						if($i % 2 == 0){
								$zebra = "even";
						} else {
								$zebra = "odd";
						}
						?>
						
						<tr id = <?= $i ?> class = "<?= $zebra ?>" >
							<td> <?= $i ?> </td>
							<td> <?= htmlentities($row[1]) ?> </td>
							<td> <?= htmlentities($row[0]) ?> </td>
							<script type="text/javascript">
								var add_but_sec = $(document.createElement("td"));
								var add_but = $(document.createElement("button"));
								add_but.innerHTML = "Add";
								add_but.value = "<?=($row[0])?>";
								
								add_but.observe("click", add_food);
								add_but_sec.appendChild(add_but);
								
								$("<?= $i ?>").appendChild(add_but_sec);
								
								if (typeof(navigator.geolocation) != 'undefined') {
									navigator.geolocation.getCurrentPosition(function(position) {	// get current position.
										var wp = new Array(2);		
										var lat = position.coords.latitude;													// for user's current location: latitude
										var lng = position.coords.longitude;												// for user's current location: longitude	
										
										var location1 = new GLatLng(lat, lng);
										var location2 = new GLatLng(<?= $row[6] ?>, <?= $row[7] ?>);
										var dist = location1.distanceFrom(location2);
										dist = dist * 0.000621371192 + 0.1;		// convert meters to miles 
										dist = Math.round(dist*100)/100 
										
										var mile = $(document.createElement("td"));
										mile.innerHTML = dist;
									
										$("<?= $i ?>").appendChild(mile);
										$("<?= $i ?>").appendChild(mile);
									});
								}
							</script>
							<td id="see_map"> 
								<a href="map.php?lat=<?= $row[6] ?>&long=<?= $row[7] ?>" >
									<img src="http://www.project-fin.org/openmenu/Front/img/map_icon.jpg" alt="map_icon" />
									See On a Map
								</a>
							</td>
						</tr>
							
						<?php
						$i++;
						$row = mysql_fetch_array($result);
					}
				}
        ?>
				</table>    
		  </div>
    <?php
	}
}

if (isset($_REQUEST["menu_rest_name"])) {
    $menu = $_REQUEST["menu_rest_name"];
}

if (isset($_REQUEST["want"])) {
    $want = $_REQUEST["want"];
}

# Check if the connection is made well; else, print error message.
check(mysql_connect("localhost", "dawgsfor_omuser", "454ftw"), "mysql_connect");

# Check if database choosing is made well; else, print error message.
check(mysql_select_db("dawgsfor_openmenu"), "mysql_select_db");

# search box
type_menu();
if($want == "Restaurant"){
	$cap = $menu." has the following menues.";
} else if ($want == "Food"){
	$cap = "The restaurants that has ".$want; 
}


if($want == "Restaurant"){
	menu_table(
#	sorting_by_dist(
		"SELECT i.name, r.name, r.address, r.city, r.state, r.country, i.description, i.price, r.lat, r.lon " .
		"From restaurants r " .
		"JOIN restaurants_items ri on ri.rid = r.rid " .
		"JOIN items i on i.iid = ri.iid " .
		"WHERE r.name LIKE ". "'%" . $menu . "%'".
		" ORDER BY i.name ASC;", $menu, $cap, $want
	);
} else if ($want == "Food"){
	menu_table(
#	sorting_by_dist(
		"SELECT i.name, r.name, r.address, r.city, r.state, r.country, r.lat, r.lon " .
		"From restaurants r " .
		"JOIN restaurants_items ri on ri.rid = r.rid " .
		"JOIN items i on i.iid = ri.iid " .
		"WHERE i.name LIKE ". "'%" . $menu . "%'".
		" ORDER BY r.name ASC;", $menu, $cap, $want
	);
}

# query. select menu name, restaurant name, restaurant's address, city, state, and country.

# call this function for validator.
bottom();
?>

