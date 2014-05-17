<?php 

	require("config.inc.php");

	if (!empty($_POST)) {

    $query = "Update users SET username = :username, name = :name,surname = :surname,
	sex= :sex, birth= :birth, nr_tel= :nr_tel, email=:email, city = :city, adress = :adress  WHERE us_id = :us_id"; 
	  $query_params = array(
	  ':us_id' => $_POST['us_id'],
	   ':username' => $_POST['username'],
        ':name' => $_POST['name'],
		':surname' => $_POST['surname'],
		':sex' => $_POST['sex'],
		':birth' => $_POST['birth'],
		':nr_tel' => $_POST['phone'],
		':email' => $_POST['email'],
		':city' => $_POST['city'],
		':adress' => $_POST['adress']
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
    $respon["message"] = "Personal data successfully updated";

    echo json_encode($respon);

} else {
?>
<form action="update.php" method="post"> 
	ID:<br /> 
	<input type="text" name="us_id" value=""; size="30"  /> 
	<br /><br /> 
	Username:<br /> 
	<input type="text" name="username" value="<?php echo $row['username']; ?>"; size="30" maxlength = "60"  /> 
	<br /><br /> 
	Name:<br /> 
	<input type="text" name="name" size="30" maxlength = "60" value="<?php echo $row['name']; ?>" /> 
	<br /><br /> 
	Surname:<br />
	<input type="text" name="surname" size="30" maxlength = "60" value="<?php echo $row['surname']; ?>" /> 
	<br /><br /> 
	Gender:<br /> 
	<input type="radio" name="sex" value= "2"<?php if ($row['sex']=='2') echo "checked";?>
	>Female
	<input type="radio" name = "sex" value="1"<?php if ($row['sex']=='1') echo "checked";?>
	>Male
	<br /><br /> 
	Birth:<br /> 
	<input type="date" name="birth" value="<?php echo $row['birth']; ?>" /> 
	<br /><br /> 
	Phone number:<br /> 
	<input type="tel" name="phone" size="30" maxlength = "11" value="<?php echo $row['nr_tel']; ?>" /> 
	<br /><br /> 
	Email:<br /> 
	<input type="email" name="email"  size="30" maxlength = "50" value="<?php echo $row['email']; ?>" /> 
	<br /><br /> 
	City:<br /> 
	<input type="text" name="city" size="30" maxlength = "50" value="<?php echo $row['city']; ?>" /> 
	<br /><br /> 
	Address:<br /> 
	<input type="text" name="adress" size="30" maxlength = "100" value="<?php echo $row['adress']; ?>" /> 
	<br /><br /> 
	<input type="submit" name = "edit" value="Update User" /> 
</form>
<?php
}

?>