<?php
require_once "conn.php";
if(isset($_GET['city_name'])){
    $sql = "select location_id, location_name from location where city_id=(select city_id from cities where city_name='".$_GET['city_name']."')";
    if(!$conn->query($sql)){
        echo "Error in executing query.";
    }else{
        $result = $conn->query($sql);
        if($result->num_rows > 0){
            $return_arr['locations'] = array();
            while($row = $result->fetch_array()){
                array_push($return_arr['locations'], array(
                    'location_id'=>$row['location_id'],
                    'location_name'=>$row['location_name']
                ));
            }
            echo json_encode($return_arr);
        }
    }
}
?>