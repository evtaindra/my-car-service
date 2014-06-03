<?php

	/*
	"config.inc.php" plik łączy się z bazą danych za każdym razem,
	kiedy wywołany jest w skrypcie php.  
	*/
require("config.inc.php");

	//jeśli wysłane dane nie są puste
if (!empty($_POST)) {
    //Jeśli nazwa użytkownika lub hasło jest puste, gdy użytkownik prześle 
    // formularz, strona przestanie działać.
    if (empty($_POST['username']) || empty($_POST['password']) || empty($_POST['name']) || empty($_POST['surname']) ||
	empty($_POST['sex']) || empty($_POST['birth']) || empty($_POST['phone']) || empty($_POST['email']) || empty($_POST['city']) || empty($_POST['adress'])  ) {
        
        
        // Tworzenie pewnych danych, które będą odpowiedzią JSON
        $response["success"] = 0;
        $response["message"] = "Please enter all required fields.";
        
        //zabijanie strony i nie wykonywanie kodu poniżej.
		//będzie to również wyświetlenie parametru.
        die(json_encode($response));
    }
    
    //jeżeli strona nie umarła, sprawdzimy w naszej bazy danych czy jest 
	//	użytkownika z imieniem użytkownika wskazanym w formularzu. 
    $query        = " SELECT 1 FROM users WHERE username = :user";
    //aktualizacja :user 
    $query_params = array(
        ':user' => $_POST['username']
    );
    
    //Uruchomienie query
    try {
        // Te dwa stwierdzenia uruchomiają kwerendę 
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($query_params);
    }
    catch (PDOException $ex) {
        
        //Wykorzystujemy to dla wytworzenia danych JSON
        $response["success"] = 0;
        $response["message"] = "Database Error1. Please Try Again!";
        die(json_encode($response));
    }
    
    //Pobieranie tablicy zwracanych danych. Jeśli jakiekolwiek dane są zwracane, 
    // wiemy, że nazwa użytkownika jest już w użyciu, więc zabijamy stronę
    $row = $stmt->fetch();
    if ($row) {

        $response["success"] = 0;
        $response["message"] = "I'm sorry, this username is already in use";
        die(json_encode($response));
    }
    
    //Jeżeli wszystko się powiodlo to tworzymy nowego użytkownika
    $query = "INSERT INTO users ( username, password, name, surname, sex, birth, nr_tel, email, city, adress) 
	VALUES ( :user, :pass, :name, :sur, :sex, :birth, :phone, :email, :city, :adress ) ";
    
    //Aktualizacja tablicy z rzeczywistymi danymi
    $query_params = array(
        ':user' => $_POST['username'],
        ':pass' => $_POST['password'],
        ':name' => $_POST['name'],
        ':sur' => $_POST['surname'],
        ':sex' => $_POST['sex'],
        ':birth' => $_POST['birth'],
        ':phone' => $_POST['phone'],
        ':email' => $_POST['email'],
        ':city' => $_POST['city'],
        ':adress' => $_POST['adress']
    );
    
    //Uruchomienie zapytania i tworzenie użytkownika
    try {
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($query_params);
    }
    catch (PDOException $ex) {
        $response["success"] = 0;
        $response["message"] = "Database Error2. Please Try Again!";
        die(json_encode($response));
    }
    
    //Jeżeli wszystko się powiodlo to dodaliśmy nowego użytkownika do bazy danych
    $response["success"] = 1;
    $response["message"] = "Username Successfully Added!";
    echo json_encode($response);

} else {
?>
	<h1>Register</h1> 
	<form action="register.php" method="post"> 
	    Username:<br /> 
	    <input type="text" name="username" size="30" maxlength = "60" value="" /> 
	    <br /><br /> 
	    Password:<br /> 
	    <input type="password" name="password" size="30" maxlength = "60" value="" /> 
	    <br /><br /> 
	    Name:<br /> 
	    <input type="text" name="name" size="30" maxlength = "60" value="" /> 
		<br /><br /> 
	    Surname:<br /> 
	    <input type="text" name="surname" size="30" maxlength = "60" value="" /> 
		<br /><br /> 
	    Gender:<br /> 
	    <input type="radio" name="sex"
		<?php if (isset($sex) && $sex=="2") echo "checked";?>
		value="2">Female
		<input type="radio" name="sex"
		<?php if (isset($sex) && $sex=="1") echo "checked";?>
		value="1">Male
		<br /><br /> 
	    Birth:<br /> 
	    <input type="date" name="birth" value="" /> 
		<br /><br /> 
	    Phone number:<br /> 
	    <input type="tel" name="phone" size="30" maxlength = "11" value="" /> 
		<br /><br /> 
	    Email:<br /> 
	    <input type="email" name="email"  size="30" maxlength = "50" value="" /> 
		<br /><br /> 
	    City:<br /> 
	    <input type="text" name="city" size="30" maxlength = "50" value="" /> 
		<br /><br /> 
	    Address:<br /> 
	    <input type="text" name="adress" size="30" maxlength = "100" value="" /> 
		<br /><br /> 
	    <input type="submit" value="Register New User" /> 
	</form>
	<?php
}

?>