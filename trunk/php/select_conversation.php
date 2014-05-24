<?php 

require("config.inc.php");

if (!empty($_POST)) {
    $query = 
	"SELECT *	
	FROM  conversations
	WHERE  sr_id = :sr_id AND us_id = :us_id";

	$query_params = array(
        ':us_id' => $_POST['us_id'],
        ':sr_id' => $_POST['sr_id']
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
		$response["messages"]   = array();
		foreach ($rows as $row) {
			$messages             = array();

			$messages["sender"]   = $row["sender"];
			$messages["date"]   = $row["date"];
			$messages["content"]   = $row["message"];
			$messages["attach"]   = $row["attach"];
			$messages["isread"]   = $row["isread"] == 1 ? true : false;		

	        array_push($response["messages"], $messages);	
		}
		echo json_encode($response);
    
	}
	else {
		$response["success"] = 0;
		$response["message"] = "No Messages!";
		die(json_encode($response));
	}
} else {
?>

<form action="select_conversation.php" method="post"> 
		<input type="text" name="us_id" value=""; size="30"  /> 
		<br /><br /> 
		<input type="text" name="sr_id" value=""; size="30"  /> 
		<br /><br /> 
		<input type="submit" name = "select" value="Select" /> 
</form>
<?php
}

?>