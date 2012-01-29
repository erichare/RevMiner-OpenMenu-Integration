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

<div id="map" style="width: 800px; height: 300px"></div>
<script type="text/javascript">

	//google.setOnLoadCallback(function() {
		// initialize the mobile map    
	var map = new GMap2(document.getElementById("map"));
	map.addControl(new GSmallMapControl());
	map.addControl(new GMapTypeControl());
	
	map.setCenter(new GLatLng(<?= $lat + 0 ?>, <?= $long + 0 ?>), 15);
	var point = new GLatLng(<?= $lat + 0 ?>, <?= $long + 0 ?>);
	map.addOverlay(new GMarker(point));
/*
	if (google.gears) {
	alert("haha");
		var geo = google.gears.factory.create('beta.geolocation');
		
		geo.getCurrentPosition(function(position) {
			var lat = position.latitude;
			var lng = position.longitude;
			var point = new GLatLng(lat, lng);
			
		//	var position = new google.maps.LatLng(lat, lng);
			map.setOptions({
			//	center: position,
				center: point,	
				zoom: 15
			});
		});
		map.addOverlay(new GMarker(point));
	}
		/*

		if (typeof(google.gears) != 'undefined') {
    var geo = google.gears.factory.create('beta.geolocation');
    geo.getCurrentPosition(function(position) {
        var lat = position.latitude;
        var lng = position.longitude;
        var position = new google.maps.LatLng(lat, lng);
        map.setOptions({
            center: position,
            zoom: 15
        });
    });
}
		*/
		
	//	map.setCenter(new GLatLng(<?= $lat + 0 ?>, <?= $long + 0 ?>), 15);
	//	var point = new GLatLng(<?= $lat + 0 ?>, <?= $long + 0 ?>);
	//	map.addOverlay(new GMarker(point));

	//});

</script>


<?php
# call this function for validator.
bottom();
?>
