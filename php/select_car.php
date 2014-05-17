<?php 

require("config.inc.php");

if (!empty($_POST)) {
    $query = "Select * FROM cars WHERE nr_id = :id";
	$query_params = array(
        ':id' => $_POST['nr_id']
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
		foreach ($rows as $row) {
		
		$response["model"]   = $row["model"];
		$response["marka"]   = $row["marka"];
		$response["nr_rej"]   = $row["nr_rej"];
		$response["silnik"]   = $row["silnik"];
		$response["przebieg"]   = $row["przebieg"];
		$response["kolor"]   = $row["kolor"];
		$response["paliwo"]   = $row["paliwo"];
		$response["rok"]   = $row["rok"];
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

<form action="select_car.php" method="post"> 
		<input type="text" name="nr_id" value=""; size="30"  /> 
		<br /><br /> 
		<input type="submit" name = "select" value="Select" /> 
</form>
<?php
}

?>