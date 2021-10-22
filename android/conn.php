<?php
$server="localhost";
$username="root";
$password="";
$database="android_multi_spinner";
$conn = mysqli_connect($server,$username,$password,$database);
if(!$conn)
    die("connection failed: ".mysqli_connect_errno);
?>