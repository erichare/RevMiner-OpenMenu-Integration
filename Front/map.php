<?php
# contact: gajok@cs.washington.edu
# filename: map.php

#include common.php for using top() and bottom() function.
include("common.php");

# This function is for <head> part.
# Also, it contains "inputs" for text and button.
top();

# search box
type_menu();

if (isset($_REQUEST["lat"])) {
    $lat = $_REQUEST["lat"];
    $lat = $lat + 0;
}

if (isset($_REQUEST["long"])) {
    $long = $_REQUEST["long"];
    $long = $long + 0;
}


?>

<div id="map" style="width: 800px; height: 400px; float:left; border: 1px solid black"></div>
<div id="route" style="width: 25%; height:480px; float:right; border: 1px solid black"></div>

<script type="text/javascript">
	var map = new GMap2(document.getElementById("map"));
	map.setUIToDefault();	// default such as mouse control
	
	if (typeof(navigator.geolocation) != 'undefined') {
		navigator.geolocation.getCurrentPosition(function(position) {
			var wp = new Array(2);
			var lat = position.coords.latitude;
			var lng = position.coords.longitude;
			wp[0] = new google.maps.LatLng(lat, lng);
			wp[1] = new google.maps.LatLng(<?= $lat + 0 ?>, <?= $long + 0 ?>);
			
			directionsPanel = document.getElementById("route");
			directions = new GDirections(map, directionsPanel);
			directions.loadFromWaypoints(wp);
		});
	}
</script>


<?php
# call this function for validator.
bottom();
?>
