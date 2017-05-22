<?php
	session_start();
	require_once("config.php");
	if (isset($_GET["Acode"])) {
		$_SESSION["Acode"] = $_GET['Acode'];
	}
	if(isset($_GET['password'])){
    	$pass = $_GET['password'];
		$acode = $_SESSION["Acode"];  // Only session vars are preserved
		print "Acode=($acode) password=($pass)<br>";
		$con=mysqli_connect(DB_HOST,DB_USER,DB_PASSWORD,DB_DATABASE);
		// Check connection
		if (mysqli_connect_errno())
  			{
  				echo "Failed to connect to MySQL: " . mysqli_connect_error();
  			}
			$query = mysqli_query($con,"SELECT * FROM Users_TU WHERE Acode='$acode'");
			$test= mysqli_num_rows($query); 
			echo "Query num rows $test <br>";
 		if (mysqli_num_rows($query)==1) 
			{
				echo "Acode matched. Checking timestamp <br>";
				$rand = rand(); // reset Acode for security
				$row = mysqli_fetch_array($query, MYSQLI_ASSOC);
				echo "after fetch random($rand)<br>";
				$startTime = $row["ADatetime"];
				$now = date('Y-m-d H:i:s');
				echo "Now=$now<br>";
			if ((round(abs($now - $startTime),2)/60) > 120) {
				echo "Your Password Session is expired.<br>";
				exit(0);
			} else {
				echo "changing pswd<br>";
				$query3="UPDATE Users SET Password='$pass', Acode='$rand' WHERE Acode='$acode'";
				// SELECT TIMESTAMPDIFF(hour,(select ADatetime from user where Acode='$acode'),now())>2)")
				$result = mysqli_query($con, $query3);
				echo "Password reset successfully. <br>";
				exit(0);
				}
		} else {
			echo 'Your Activation Code Expired. Please try again <br>';
		}
		exit(0);
	}
?>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../../favicon.ico">

    <title>Register to use TUinform</title>

    <!-- Bootstrap core CSS -->
    <link href="../css/bootstrap.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="../css/signin.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="../../../assets/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    
    
	
  </head>

  <body>

	Activation code = <?php echo $_SESSION["Acode"] ?>
    <div class="container">
	  <h1> <center> Tuinform </center> </h1>
      <form class="form-register" action="passwordReminder.php" method="GET" data-ajax="false">
        <h2 class="form-reg-heading">Reset Password</h2>
        <label for="Acode" class="sr-only">Actication Code</label>
        <input type="number" name="Acode"  class="form-control"  placeholder="Your Actication Code" required>
        <label for="password" class="sr-only">Password</label>
        <input type="password" id="password" name="password" class="form-control" placeholder="Password" required autofocus>
        
        
        <button class="btn btn-lg btn-primary btn-block" type="submit"> Reset </button>
      </form>
      
    </div> <!-- /container -->


    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="../../../assets/js/ie10-viewport-bug-workaround.js"></script>
</form>
</body>
</html>
