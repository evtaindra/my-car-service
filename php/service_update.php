<?php 

	require("config.inc.php");

	if (!empty($_POST)) {

    $query = "Update services SET name = :name, city = :city,adress = :adress,
	opis= :opis, image= :image WHERE sr_id = :sr_id"; 
	  $query_params = array(
	  ':sr_id' => $_POST['sr_id'],
	   ':name' => $_POST['name'],
        ':city' => $_POST['city'],
		':adress' => $_POST['adress'],
		':opis' => $_POST['opis'],
		':image' => $_POST['image']
    );
    try {
        $stmt   = $db->prepare($query);
		$result = $stmt->execute($query_params);
    }
    catch (PDOException $ex) {
        $respon["success"] = 0;
        $respon["message"] = "Database Error. Please Try Again!";
        die(json_encode($respon));
    }

    $respon["success"] = 1;
    $respon["message"] = "Service data successfully updated";

    echo json_encode($respon);

} else {
?>
<form action="service_update.php" method="post"> 
		ID:<br /> 
	    <input type="number_format" name="sr_id" size="30"  value="" /> 
	    <br /><br />
		Name:<br /> 
	    <input type="text" name="name" size="30" maxlength = "64" value="<?php echo $row['name']; ?>" /> 
	    <br /><br /> 
	    City:<br /> 
	    <input type="text" name="city" size="30" maxlength = "64" value="<?php echo $row['city']; ?>" /> 
	    <br /><br /> 
	    Address:<br /> 
	    <input type="text" name="adress" size="30" maxlength = "64" value="<?php echo $row['adress']; ?>" /> 
		<br /><br /> 
	    Description:<br /> 
	    <input type= "text" name="opis" size="30" maxlength = "300" value="<?php echo $row['opis']; ?>" /> 
		<br /><br /> 
	    Image:<br /> 
	    <input type="text" name="image" size="30" value="<?php echo $row['image']; ?>" /> 
		<br /><br /> 
	    
	    <input type="submit" name = "edit" value="Update Service" /> 
</form>
<?php
}

?>