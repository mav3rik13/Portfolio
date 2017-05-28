<?php
	
	require_once("config.php");
	session_start();
	error_reporting(0);
	
	if (isset($_GET['Acode']))
	{
		//keep it inside
		$Acode=$_GET['Acode'];
		$Email=$_GET['email'];
		print "Acode = $Acode<br>";
		
	
		// Check database to see if there is a matching Acode?
		$con=mysqli_connect(DB_HOST,DB_USER,DB_PASSWORD,DB_DATABASE);
		// Check connection
		if (mysqli_connect_errno())
		{
			echo "Failed to connect to MySQL: " . mysqli_connect_error();
		}
		$query = mysqli_query($con,"SELECT * from Users_TU where Acode='$Acode' and Email='$Email'")
			or die(mysqli_error($con));
		$count = mysqli_num_rows($query);
		print "num_rows = ($count) <br>";
		print "email = $Email <br>";

		if ($count > 0)
		{	// Found a matching Acode!! Set Status = 1
			$row = mysqli_fetch_array($query);
			$_SESSION['ID'] = $row['ID'];
			$_SESSION['Email'] = $row['Email'];
			$code = rand(); 
			$datetime = date('Y-m-d H:i:s'); // Record registration datetime
			print "Acode=$code, ADatetime=$datetime <br>";
			$query = mysqli_query($con,
		"UPDATE Users_TU set Status=1, Acode='$code', ADatetime='$datetime' where Acode='$Acode'")
				or die(mysqli_error($con));
			print "Database updated <br>";
			$_SESSION['ID']=0; //Not logged in yet			
			header("Location:../index.html"); // Should go back to index.php  JYS
			exit(0);
		}
	} 
	
?>
