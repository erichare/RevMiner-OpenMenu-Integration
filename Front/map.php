<?php
# author: Donghyo Min
# filename: map.php

#include common.php for using top() and bottom() function.
include("common.php");

# This function is for <head> part.
# Also, it contains "inputs" for text and button.
top();

# search box
type_menu();

if (isset($_REQUEST["lat"])) {
    $latitude = $_REQUEST["lat"];
}

if (isset($_REQUEST["long"])) {
    $longitude = $_REQUEST["long"];
}
?>

<div id="map" style="width: 400px; height: 300px"></div>

<script type="text/javascript">

//<![CDATA[
var map = new GMap(document.getElementById("map"));

map.centerAndZoom(new GPoint(<?= $latitude ?>, <?= $longitude ?>), 3);



//]]>

</script>


<?php
# call this function for validator.
bottom();
?>
