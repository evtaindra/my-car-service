<?php

require("config.inc.php");

if (!empty($_POST)) {

    if (empty($_POST['sname']) || empty($_POST['scity']) || empty($_POST['saddress']) || empty($_POST['sdescription']) || empty($_POST['suserid']) ) {
        // Create some data that will be the JSON response 
        $response["success"] = 0;
        $response["message"] = "Please enter all required fields.";

        die(json_encode($response));
    }
    

    $query = "INSERT INTO services ( name, city, adress, opis, image, us_id) 
	VALUES ( :name, :city, :adress, :opis, :image, :us_id ) ";

    $query_params = array(
        ':name' => $_POST['sname'],
        ':city' => $_POST['scity'],
        ':adress' => $_POST['saddress'],
        ':opis' => $_POST['sdescription'],
        ':image' => $_POST['simage'],
        ':us_id' => $_POST['suserid']
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
    $response["message"] = "Service Successfully Added!";

    echo json_encode($response);

} else {

?>

	<h1>Register</h1> 

	<form action="service_register.php" method="post"> 
	    Service name:<br /> 
	    <input type="text" name="sname" size="30" maxlength = "60" value="" /><br/> 
	    City:<br /> 
	    <input type="text" name="scity" size="30" maxlength = "50" value="" /> <br/> 
		Address:<br /> 
        <input type="text" name="saddress" size="30" maxlength = "100" value="" /> <br/> 
        Description:<br /> 
        <input type="text" name="sdescription" size="30" value="" /> <br/> 
        Image:<br /> 
        <input type="text" name="simage" size="30" value="" /> <br/>
         
        <input type="hidden" name="suserid" size="30" value="" /> 

		<br /><br /> 
	    <input type="submit" value="Register New Service" /> 

	</form>

	<?php

}
?>