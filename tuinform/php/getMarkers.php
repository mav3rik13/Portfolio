<?php
	session_start();
	require_once("config.php");
	
	$link = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
	if (!$link) { die("DB connection failed".mysql_error()); }
	if (!$link) { die("Unable to select database");};

	//$query = "SELECT rlocation from Report WHERE Reportcol='P';";
	//$result = mysqli_query($link,$query)or die(mysql_error());
    //$markers = mysql_fetch_array($result);
	$lat= array(39.978,39.984, 39.983);
	
	
	$lng=array(-75.155,-75.152,-75.155);
	
	$lat = implode(",", $lat);
	$lng=implode(",", $lng);
	mysqli_close($link);
?>
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&signed_in=true"></script>
<script type="text/javascript">

var map;


function initialize() {
	//var locations="<?php echo $markers; ?>";
	var myLatlng = new google.maps.LatLng(39.981, -75.155);
  var mapOptions = {
    zoom: 16,
    center: new google.maps.LatLng(39.981, -75.155)
  };
  
  
  
  
  
  /*var locations = [
	[39.978,-75.155],
	[39.984, -75.152],
	[39.983, -75.155]
	];*/
	
	
	
	var LAT= <?php echo json_encode($lat); ?>;
	var LNG= <?php echo json_encode($lng); ?>;
	
  map = new google.maps.Map(document.getElementById('map-canvas'),
      mapOptions);
	  

  var marker, i;
  
  for (i = 0; i <= LAT.length; i++) {  
      marker = new google.maps.Marker({
        position: new google.maps.LatLng(LAT[i], LNG[i]),
        map: map
      });}
}



google.maps.event.addDomListener(window, 'load', initialize);

    </script>