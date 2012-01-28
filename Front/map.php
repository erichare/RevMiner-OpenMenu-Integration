<?php
# author: Donghyo Min
# filename: search.php

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
<script type="text/javascript" src="http://www.google.com/jsapi?key=ABQIAAAAha3tZBPtoRIZLy8L__KptBQnG3kJyZKQJZVr4eR4kYFSiXUT9RRFxcXjJhP2jKzqq_rH2oTI3uqs_w&autoload=%7Bmodules%3A%5B%7Bname%3A%22maps%22%2Cversion%3A3%2Cother_params%3A%22sensor%3Dtrue%22%7D%5D%7D"></script>
<script type="text/javascript" src="gears_init.js"></script>
<div id="map" style="width: 800px; height: 300px"></div>

<script type="text/javascript">
google.setOnLoadCallback(function() {
	var map = new GMap2(document.getElementById("map"));
	map.addControl(new GSmallMapControl());
	map.addControl(new GMapTypeControl());
	// initialize the mobile map    
//	if (typeof(google.gears) != 'undefined') {
	var geo = google.gears.factory.create('beta.geolocation');
	geo.getCurrentPosition(function(position) {
			var lat = position.latitude;
			var lng = position.longitude;
			var position = new google.maps.LatLng(lat, lng);
			map.addOverlay(new GMarker(position));
        map.setOptions({
					center: position,
					zoom: 15
					});
			});
//	}

	
	
//	map.setCenter(new GLatLng(<?= $lat + 0 ?>, <?= $long + 0 ?>), 15);
//	var point = new GLatLng(<?= $lat + 0 ?>, <?= $long + 0 ?>);
//	map.addOverlay(new GMarker(point));

});

</script>


<?php
# call this function for validator.
bottom();
?>
