<?php

	
        require_once("config.php");
	session_start();
        error_reporting(0);

    		//keep it inside
			$email=$_POST['inputEmail'];
			$password=$_POST['inputPassword'];
			print "Email = $email paswd=($password)<br>";
			
			$con=mysqli_connect(DB_HOST,DB_USER,DB_PASSWORD,DB_DATABASE);
			// Check connection
			if (mysqli_connect_errno())
			{
				echo "Failed to connect to MySQL: " . mysqli_connect_error();
			}
			$query = mysqli_query($con,"SELECT * from Users_TU where Email='$email'")
				or die(mysqli_error($con));
			$count = mysqli_num_rows($query);
			print "num_rows = ($count) <br>";
			if ($count > 0)
			{	// Email already exists 
				$_SESSION['ID']=-3;
				header("Location: ../index.html#page2");
				exit(0);
			}
			print "Database changed <br>";			
			// Insert into DB but set status = 0 inactive
			$code = rand(); // Generate an activation code
			$datetime = date('Y-m-d H:i:s'); // Record registration datetime
			print "Acode=$code, ADatetime=$datetime <br>";
			
			$query = mysqli_query($con,"INSERT INTO Users_TU (Email, Password, Status, Acode, RDatetime,ADatetime) values
			('$email','$password',0,'$code','$datetime','$datetime')")
				or die(mysqli_error($con));
			print "Database changed <br>";			
			// Send email to confirm email
			$message="Please click the following link to activate your registration: 
				http://cis-linux2.temple.edu/~tuf08921/tuInform/php/confirmRegistration.php?email=$email&Acode=$code ";
			$header = "Tu Inform Registration.";
					$header = 'MIME-Version: 1.0' . "\r\n";
					$header .= 'Content-type: text/html; charset=iso-8859-1' . "\r\n";
					$header .= 'From: tuf08921@temple.edu' . "\r\n";				
			mail($email, "Account Registration Confirmation", $message, $header);
			
			echo 'Email sent';
			session_unset();
			header("Location:../index.html");
			exit(0);
		
		
?>
