<?php 

require("config.inc.php");

if (!empty($_POST)) {
    $query = 
	"Select  
		services.sr_id, 
		services.name,
		services.city,
		services.adress ,
		services.mark ,
		services.opis ,
		services.image ,
		services.us_id,
		users.nr_tel,
		users.email
	FROM  services 
	INNER JOIN  users ON  services.us_id =  users.us_id
	WHERE  sr_id = :id";

	$query_params = array(
        ':id' => $_POST['sr_id']
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
		
		$response["sid"]   = $row["sr_id"];
		$response["sname"]   = $row["name"];
		$response["scity"]   = $row["city"];
		$response["saddress"]   = $row["adress"];
		$response["srating"]   = $row["mark"];	
		$response["simage"]   = $row["image"];
		$response["sus_id"]   = $row["us_id"];
		$response["snr_tel"]   = $row["nr_tel"];
		$response["semail"]   = $row["email"];
		$response["sdescription"]   = $row["opis"];
	}
		echo json_encode($response);
    
}
	else {
		$response["success"] = 0;
		$response["message"] = "No Service In System!";
		die(json_encode($response));
		 }
	

} else {
?>

<form action="select_service.php" method="post"> 
		<input type="text" name="sr_id" value=""; size="30"  /> 
		<br /><br /> 
		<input type="submit" name = "select" value="Select" /> 
</form>
<?php
}

?>