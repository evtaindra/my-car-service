<?php 

    // Te zmienne okreslaja informacje o polaczeniu do bazy danych MySQL
	$host = "localhost"; 
	$dbname = "u546627701_carse";     
	$username = "u546627701_admin"; 
    $password = "qwerty12345"; 

    // UTF-8 jest schematem kodowania znakow, ktory pozwala na wygodne przechowywanie
    // wielkiego wyboru znakow specjalnych w swojej bazie danych.
    // Przekazujac nastepujaca tablice $ Opcje do kodu polaczenia z baza danych
    // mowimy serwerowi MySQL, ze chcemy sie z nim komunikowac za pomoca UTF-8
    $options = array(PDO::MYSQL_ATTR_INIT_COMMAND => 'SET NAMES utf8'); 
   
    // Jesli w dowolnym momencie napotka na blad podczas wykonywania tego kodu, to zatrzymuje sie
    //  i skacze w dol do bloku catch.
    try 
    { 
        // Otwierannie polaczenie z baza danych przy uzyciu biblioteki PDO
        $db = new PDO("mysql:host={$host};dbname={$dbname};charset=utf8", $username, $password, $options); 
    } 
    catch(PDOException $ex) 
    { 
        // W przypadku wystapienia bledu podczas otwierania polaczenia do bazy danych,
        // to bedzie zawieszona tutaj.
        die("Failed to connect to the database: " . $ex->getMessage()); 
    } 
     
    // PDO rzuci wyjatek, gdy napotka sie na blad. 
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION); 
     
    //  Konfiguracja PDO, aby powrocic wierszy bazy danych za pomoca tablicy asocjacyjnej.
	//	Oznacza to, ze tablica bedzie miala indeksy, w ktorym wartosc  
	//  reprezentuje nazwe kolumny w bazie danych.
    $db->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC); 
     

    if(function_exists('get_magic_quotes_gpc') && get_magic_quotes_gpc()) 
    { 
        function undo_magic_quotes_gpc(&$array) 
        { 
            foreach($array as &$value) 
            { 
                if(is_array($value)) 
                { 
                    undo_magic_quotes_gpc($value); 
                } 
                else 
                { 
                    $value = stripslashes($value); 
                } 
            } 
        } 
     
        undo_magic_quotes_gpc($_POST); 
        undo_magic_quotes_gpc($_GET); 
        undo_magic_quotes_gpc($_COOKIE); 
    } 
     
    // Mowi to przegladarce, ze tresc jest zakodowana z uzyciem UTF-8 
    // i, ze powinna przeslac zawartosc z powrotem za pomoca UTF-8 
    header('Content-Type: text/html; charset=utf-8'); 
     
    // Inicjacja sesji. Sesje sa uzywane do przechowywania informacji 
	// na temat uzytkownika, kiedyon przechodzi z jednej strony internetowej do drugiej.
    session_start(); 

?>
