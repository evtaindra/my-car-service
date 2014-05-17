<?php 

require("config.inc.php");

if (!empty($_POST)) {
    $query = "Select * FROM users WHERE us_id = :id";
	$query_params = array(
        ':id' => $_POST['us_id']
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
		
		$response["username"]   = $row["username"];
		$response["password"]   = $row["password"];
		$response["name"]   = $row["name"];
		$response["surname"]   = $row["surname"];
		$response["sex"]   = $row["sex"];
		$response["birth"]   = $row["birth"];
		$response["nr_tel"]   = $row["nr_tel"];
		$response["email"]   = $row["email"];
		$response["city"]   = $row["city"];
		$response["adress"]   = $row["adress"];
	}
		echo json_encode($response);
    
}
	else {
		$response["success"] = 0;
		$response["message"] = "No Users In System!";
		die(json_encode($response));
		 }
	

} else {
?>

<form action="select_user.php" method="post"> 
		<input type="text" name="us_id" value=""; size="30"  /> 
		<br /><br /> 
		<input type="submit" name = "select" value="Select" /> 
</form>
<?php
}

?>