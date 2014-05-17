<?php 

	require("config.inc.php");

	if (!empty($_POST)) {

    $query = "Update cars SET model = :model, marka = :marka,nr_rej = :nr_rej,
	silnik= :silnik, przebieg= :przebieg, kolor= :kolor, paliwo=:paliwo, rok = :rok WHERE nr_id = :nr_id"; 
	  $query_params = array(
	  ':nr_id' => $_POST['nr_id'],
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
        $respon["success"] = 0;
        $respon["message"] = "Database Error. Please Try Again!";
        die(json_encode($respon));
    }

    $respon["success"] = 1;
    $respon["message"] = "User data successfully updated";

    echo json_encode($respon);

} else {
?>
<form action="car_update.php" method="post"> 
		ID:<br /> 
	    <input type="number_format" name="nr_id" size="30"  value="" /> 
	    <br /><br />
		Model:<br /> 
	    <input type="text" name="model" size="30" maxlength = "30" value="<?php echo $row['model']; ?>" /> 
	    <br /><br /> 
	    Brand:<br /> 
	    <input type="text" name="marka" size="30" maxlength = "30" value="<?php echo $row['marka']; ?>" /> 
	    <br /><br /> 
	    Registration number:<br /> 
	    <input type="text" name="nr_rej" size="30" maxlength = "8" value="<?php echo $row['nr_rej']; ?>" /> 
		<br /><br /> 
	    Engine:<br /> 
	    <input type= "number" name="silnik" size="30" step = "0.01" value="<?php echo $row['silnik']; ?>" /> 
		<br /><br /> 
	    Mileage:<br /> 
	    <input type="number_format" name="przebieg" size="30" value="<?php echo $row['przebieg']; ?>" /> 
		<br /><br /> 
	    Color:<br /> 
	    <input type="text" name="color" size="30" maxlength = "30" value="<?php echo $row['kolor']; ?>" /> 
		<br /><br /> 
	    Fuel:<br /> 
	    <input type="text" name="fuel"  size="30" maxlength = "30" value="<?php echo $row['paliwo']; ?>" /> 
		<br /><br /> 
	    Year:<br /> 
	    <input type="number_format" name="year" size="30" maxlength = "11" value="<?php echo $row['rok']; ?>" /> 
		<br /><br /> 
	    <input type="submit" name = "edit" value="Update Car" /> 
</form>
<?php
}

?>