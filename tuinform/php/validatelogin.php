<?php
	header('Access-Control-Allow-Origin: *');
	session_start();
	if (!isset($_SESSION["LoginState"])) echo "Null";
	else echo $_SESSION["LoginState"]." ".$_SESSION["Email"]." ".$_SESSION["isAdmin"];
?>
