<?php
require_once "conn.php";
$sql = "select * from countries";
if(!$conn->query($sql)){
    echo "Error in connecting to Database.";
}
else{
    $result = $conn->query($sql);
    if($result->num_rows > 0){
        $return_arr['countries'] = array();
        while($row = $result->fetch_array()){
            array_push($return_arr['countries'], array(
                'country_id'=>$row['country_id'],
                'country_name'=>$row['country_name']
            ));
        }
        echo json_encode($return_arr);
    }
}
?>