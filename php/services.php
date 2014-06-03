<?php
require("config.inc.php");

$query = "Select * FROM services";

try {
    $stmt   = $db->prepare($query);
    $result = $stmt->execute();
}
catch (PDOException $ex) {
    $response["success"] = 0;
    $response["message"] = "Database Error!";
    die(json_encode($response));
}

$rows = $stmt->fetchAll();


if ($rows) {
    $response["success"] = 1;
    $response["message"] = "Servises Available!";
    $response["services"]   = array();
    
    foreach ($rows as $row) {
        $post             = array();
        $post["sid"] = $row["sr_id"];
        $post["sname"]    = $row["name"];
        $post["scity"]  = $row["city"];
        $post["saddress"]  = $row["adress"];
        $post["srating"]  = $row["mark"];
        $post["simage"]  = $row["image"];
        $post["sus_id"]  = $row["us_id"];
        
        array_push($response["services"], $post);
    }
    
    echo json_encode($response);
    
    
} else {
    $response["success"] = 1;
    $response["message"] = "No Services In System!";
    die(json_encode($response));
}

?>
