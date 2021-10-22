<?php
require_once "conn.php";
if(isset($_GET['country_name']) && isset($_GET['city_name']) && isset($_GET['location_name'])){
    $sql = "select image_path from images where country_id=(select country_id from countries where country_name='" . $_GET['country_name'] . "') and city_id=(select city_id from cities where city_name='" . $_GET['city_name'] . "') and location_id=(select location_id from location where location_name='" . $_GET['location_name'] . "')";
    //echo $sql;
    if(!$conn->query($sql)){
        echo "Error in executing query.";
    }else{
        $result = $conn->query($sql);
        if($result->num_rows > 0){
        $row = $result->fetch_array();
	echo $row['image_path'];
        }
    }
}
?>