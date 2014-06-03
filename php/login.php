<?php



//załadowanie i podłączenie sie do bazy danych MySQL

require("config.inc.php");



if (!empty($_POST)) {

    // pobieranie informacje użytkownika opierajac o nazwę użytkownika.

    $query = " 

            SELECT 

                us_id, 

                username, 

                password

            FROM users 

            WHERE 

                username = :username 

        ";

    

    $query_params = array(

        ':username' => $_POST['username']

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

    

    //Zmienna w celu ustalenia, czy informacje użytkownika jest prawidłowe.

    $validated_info = false;

    

    //ściąganie wszystkich wierszy 

    $row = $stmt->fetch();

    if ($row) {

        if ($_POST['password'] === $row['password']) {

            $login_ok = true;

        }

    }

    // Jeśli logowanie sie powiodlo, to wyślijmy użytkownika do prywatnych członków
    // W przeciwnym razie wyświetlamy komunikat że logowanie nie powiodło się
	//	i pokazujemy ponownie formularz logowania

    if ($login_ok) {

        $response["success"] = 1;

        $response["message"] = $row['us_id'];

        die(json_encode($response));

    } else {

        $response["success"] = 0;

        $response["message"] = "Invalid Credentials!";       

        die(json_encode($response));

    }

} else {

?>

		<h1>Login</h1> 

		<form action="login.php" method="post"> 

		    Username:<br /> 

		    <input type="text" name="username" size="30" maxlength = "60" placeholder="username" /> 

		    <br /><br /> 

		    Password:<br /> 

		    <input type="password" name="password" size="30" maxlength = "60" placeholder="password" value="" /> 

		    <br /><br /> 

		    <input type="submit" value="Login" /> 

		</form> 

		<a href="register.php">Register</a>

	<?php

}



?> 

