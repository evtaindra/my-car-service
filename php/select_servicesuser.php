<?php 

require("config.inc.php");

if (!empty($_POST)) {
    $query = "Select * FROM services WHERE us_id = :us_id";
	$query_params = array(
        ':us_id' => $_POST['us_id']
    );
    
    try {
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($query_params);
    }
    catch (PDOException $ex) {
        $response["success"] = 0;
        $response["message"] = "Database Error. Please Try Again!";
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
    
    
} 
	else {
		$response["success"] = 0;
		$response["message"] = "No Cars In System!";
		die(json_encode($response));
		 }

} else {
?>

<form action="select_servicesuser.php" method="post"> 
		<input type="text" name="us_id" value=""; size="30"  /> 
		<br /><br /> 
		<input type="submit" name = "select" value="Select" /> 
</form>
<?php
}

?>