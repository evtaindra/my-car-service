<?php 

require("config.inc.php");

if (!empty($_POST)) {
    $query = "call insertOrUpdateUserMark(:us_id,:sr_id,:mark)";
	$query_params = array(
        ':us_id' => $_POST['us_id'],
        ':sr_id' => $_POST['sr_id'],
        ':mark' => $_POST['mark']
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
    
    $query = "SELECT mark FROM services WHERE sr_id = :sr_id";
    $query_params = array(
        ':sr_id' => $_POST['sr_id']
    );
   
    try {
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($query_params);
    }
    catch (PDOException $ex) {
        $response["success"] = 0;
        $response["message"] = "Database Error1. Please Try Again!";
        die(json_encode($response));
    }

    $row = $stmt->fetch();
    if ($row) {
        $response["success"] = 1;
        $response["message"] = $row["mark"];
        die(json_encode($response));
    }
   
} else {
?>

<form action="rate_service.php" method="post"> 
		<input type="text" name="us_id" value=""; size="30"  /> 
		<br />
		<input type="text" name="sr_id" value=""; size="30"  /> 
		<br />
		<input type="text" name="mark" value=""; size="30"  /> 
		<br />
		<input type="submit" name = "select" value="Select" /> 
</form>
<?php
}

?>