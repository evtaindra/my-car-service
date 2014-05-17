<?php

require("config.inc.php");

if (!empty($_POST)) {

    if (empty($_POST['us_id']) || empty($_POST['model']) || empty($_POST['marka']) || empty($_POST['nr_rej']) || empty($_POST['silnik']) ||
	empty($_POST['przebieg']) || empty($_POST['color']) || empty($_POST['fuel']) || empty($_POST['year']) ) {
 
        $response["success"] = 0;
        $response["message"] = "Please enter all required fields.";
        
        die(json_encode($response));
    }

    $query = "INSERT INTO cars ( us_id, model, marka, nr_rej, silnik, przebieg, kolor, paliwo, rok) 
	VALUES ( :us_id, :model, :marka, :nr_rej, :silnik, :przebieg, :kolor, :paliwo, :rok) ";
    
    $query_params = array(
        ':us_id' => $_POST['us_id'],
        ':model' => $_POST['model'],
        ':marka' => $_POST['marka'],
        ':nr_rej' => $_POST['nr_rej'],
        ':silnik' => $_POST['silnik'],
        ':przebieg' => $_POST['przebieg'],
        ':kolor' => $_POST['color'],
        ':paliwo' => $_POST['fuel'],
        ':rok' => $_POST['year']
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
    
    $response["success"] = 1;
    $response["message"] = "Car Successfully Added!";
    echo json_encode($response);
  
} else {
?>
	<h1>Car Register</h1> 
	<form action="car_register.php" method="post"> 
		Us_id:<br /> 
	    <input type="number_format" name="us_id" size="30"  value="" /> 
	    <br /><br /> 
		Model:<br /> 
	    <input type="text" name="model" size="30" maxlength = "30" value="" /> 
	    <br /><br /> 
	    Brand:<br /> 
	    <input type="text" name="marka" size="30" maxlength = "30" value="" /> 
	    <br /><br /> 
	    Registration number:<br /> 
	    <input type="text" name="nr_rej" size="30" maxlength = "8" value="" /> 
		<br /><br /> 
	    Engine:<br /> 
	    <input type= "number" name="silnik" size="30" step = "0.01" value="" /> 
		<br /><br /> 
	    Mileage:<br /> 
	    <input type="number_format" name="przebieg" size="30" value="" /> 
		<br /><br /> 
	    Color:<br /> 
	    <input type="text" name="color" size="30" maxlength = "30" value="" /> 
		<br /><br /> 
	    Fuel:<br /> 
	    <input type="text" name="fuel"  size="30" maxlength = "30" value="" /> 
		<br /><br /> 
	    Year:<br /> 
	    <input type="number_format" name="year" size="30" maxlength = "11" value="" /> 
		<br /><br /> 
	    <input type="submit" value="Register New Car" /> 
	</form>
	<?php
}

?>