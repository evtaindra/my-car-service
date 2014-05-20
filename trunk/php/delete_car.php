<?php

require("config.inc.php");

if (!empty($_POST)) {


    $query = "DELETE FROM cars WHERE nr_id =  :nr_id";
    
    $query_params = array(
        ':nr_id' => $_POST['nr_id']);
    
    try {
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($query_params);
    }
    catch (PDOException $ex) {
        $response["success"] = 0;
        $response["message"] = "Database Error. Please Try Again!";
        die(json_encode($response));
    }
    
    $response["success"] = 1;
    $response["message"] = "Car Successfully deleted!";
    echo json_encode($response);
  
} else {
?>
	<h1>Car Register</h1> 
	<form action="delete_car.php" method="post"> 
		nr_id:<br /> 
	    <input type="text" name="nr_id" value=""; size="30"  /> 
	    <br /><br /> 
	    <input type="submit" value="Delete Car" /> 
	</form>
	<?php
}

?>