<?php
        require_once("config.php");
        error_reporting(0);
                //keep it inside
                $email=$_POST['inputEmail'];
                $con=mysqli_connect(DB_HOST,DB_USER,DB_PASSWORD,DB_DATABASE);
                // Check connection
                if (mysqli_connect_errno())
                {
                        echo "Failed to connect to MySQL: " . mysqli_connect_error();
                }
                $query = mysqli_query($con,"SELECT * FROM Users_TU WHERE Email='$email'")
                        or die(mysqli_error($con));

                if (mysqli_num_rows ($query)==1)
                {
                        $code=rand();
						$message="Please click the following link to reset password:
                        http://cis-linux2.temple.edu/~tuf08921/tuInform/php/passwordReset.php?email=$email&Acode=$code";
						$header = "Tu Inform Reset Password.";
					    $header = 'MIME-Version: 1.0' . "\r\n";
					    $header .= 'Content-type: text/html; charset=iso-8859-1' . "\r\n";
					    $header .= 'From: tuf08921@temple.edu' . "\r\n";				
                        mail($email, "TU Inform Password Reset Request", $message,$header);
                        echo 'Email sent';
                        $datetime = date('Y-m-d H:i:s');
                        $query2 = mysqli_query($con,"UPDATE Users_TU SET Acode='$code', ADatetime='$datetime' WHERE Email='$email'");
                    	if (!$query){$_SESSION ["LoginState"] = -5;}
                    	else { $_SESSION ["LoginState"] = 0;}
                } else {
                        print "$email was not registered. <br>";
                        $_SESSION ["LoginState"] = -4;
                }
                header("Location:../index.html");
        
?>
