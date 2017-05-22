<?php 
	session_start();
	require_once("config.php");

	print "Passed contants<br>"; 

	print "Host=".DB_HOST." !!!!<br>";
	print "Login=".DB_USER." !!!<br>";
	print "Pswd=".DB_PASSWORD."!!!!<br>";
	print "Schema=".DB_DATABASE."!!!<br>";

	$link = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
	if (!$link) { die("DB connection failed".mysql_error()); }
	print "Connect success <br>";
	if (!$link) { die("Unable to select database");};
	print "DB selected <br>";
	$Email= $_POST["inputEmail"];
	$Paswd = $_POST["inputPassword"];
	print "the email ($Email) password ($Paswd)<br>";
	print "Input check passed<br>";

	// .md5($_POST['password'])
	$query = "SELECT * from User WHERE Email='$Email' and Password= '$Paswd';";
	$result = mysqli_query($link,$query);

	if ($result)	{
		if (mysqli_num_rows($result) == 1)	{//Login Successful
			session_regenerate_id();
			$dataset = mysqli_fetch_assoc($result);
			$_SESSION["Email"]= $dataset["Email"];
			$_SESSION["isAdmin"] = $dataset["isAdmin"];
			print "ID = (".$dataset["ID"].") <br>";
			$_SESSION["LoginState"]= 1; // Successful
			header("Location:../mainboot.html");
			exit();
		} else {		//Login Failed
			$_SESSION["LoginState"]= -1;
			print "Login Failed<br>";
			header("location:../index.html");
		}
	} else{ 
	
		$_SESSION["LoginState"] = -1;
		header("Location:../index.html");
		die("Query failed".mysql_error());
	}

?>
This is HTML
