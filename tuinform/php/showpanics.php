<?php 
	session_start();
	require_once("config.php");

	$link = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
	if (!$link) { die("DB connection failed".mysql_error()); }
	if (!$link) { die("Unable to select database");};
	
	$query = "SELECT a.*,b.Email from Report a, Users_TU b WHERE a.Reportcol='P' and a.uId = b.ID order by a.rDatetime desc;";
	$result = mysqli_query($link,$query);
	echo '<table border = 1>';
        echo '<tr>';
            echo ' <th>User ID</th>';
            echo '<th>Email Address</th>';
            echo ' <th>Phone Number</th>';
            echo ' <th>Call Back</th>';
			echo ' <th>Comment</th>';
			echo ' <th>Time Reported</th>';
			echo ' <th>Location</th>';
        echo ' </tr>';

	 while($row=mysqli_fetch_array($result))
    {
        echo "<tr>";
        echo "<td>" . $row['uId'] . "</td>";
        echo "<td>" . $row['Email'] . "</td>";
        echo "<td>" . $row['rCall'] . "</td>";
		echo "<td>" . $row['rType'] . "</td>";
		echo "<td>" . $row['rComment'] . "</td>";
		echo "<td>" . $row['rDatetime'] . "</td>";
		echo "<td>" . $row['rlocation'] . "</td>";

        echo "</tr>";
    }
	 mysqli_close($link);
    echo '</table>';
?>
