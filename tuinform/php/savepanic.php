<?php 
	session_start();
	require_once("config.php");

	$incidentinput= $_POST["incidentinput"];
	$phonenumber = $_POST["phonenumber"]; 
	$callback = $_POST["callback"]; 
	$Email = $_SESSION["Email"];
	$location = $_POST["location"];
	$lat=$_POST["LAT"];
	$lng=$_POST["LNG"];

	if($callback ==""){
		$callback = "No";
	}
	$datetime = date('Y-m-d H:i:s'); // Record registration datetime
	$con=mysqli_connect(DB_HOST,DB_USER,DB_PASSWORD,DB_DATABASE);
			// Check connection
			if (mysqli_connect_errno())
			{
				echo "Failed to connect to MySQL: " . mysqli_connect_error();
			}
			
			
		$query = mysqli_query($con,"SELECT * FROM Users_TU WHERE Email='$Email'");
			if (mysqli_num_rows($query)==1) 
			{
				$row = mysqli_fetch_array($query, MYSQLI_ASSOC);
				$uid= $row["ID"];
				$query1 = mysqli_query($con,"INSERT INTO Report (rDatetime, rComment, rCall,rType,Reportcol,uId,rlocation) values
				('$datetime','$incidentinput',$phonenumber,'$callback','P',$uid,'$location')")
					or die(mysqli_error($con));
				
				header("Location:../mainboot.html");
				exit();
			}else {		//Login Failed
			$_SESSION["LoginState"]= -1;
			print "Login Failed<br>";
			header("location:../index.html");
		}
	
	
?>
This is HTML
