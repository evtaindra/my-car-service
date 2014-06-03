<?php
require("config.inc.php");

if (!empty($_POST)) {
$query = "Select * FROM cars WHERE us_id = :us_id";
	$query_params = array(
        ':us_id' => $_POST['us_id']
    );

try {
    $stmt   = $db->prepare($query);
    $result = $stmt->execute($query_params);
}
catch (PDOException $ex) {
    $response["success"] = 0;
    $response["message"] = "Database Error!";
    die(json_encode($response));
}

$rows = $stmt->fetchAll();


if ($rows) {
    $response["success"] = 1;
    $response["message"] = "Cars Available!";
    $response["cars"]   = array();
    
    foreach ($rows as $row) {
        $post             = array();
        $post["nr_id"] = $row["nr_id"];
        $post["model"]    = $row["model"];
        $post["marka"]  = $row["marka"];
        $post["nr_rej"]  = $row["nr_rej"];
        $post["silnik"]  = $row["silnik"];
        $post["przebieg"]  = $row["przebieg"];
        $post["kolor"]  = $row["kolor"];
		$post["paliwo"]  = $row["paliwo"];
        $post["rok"]  = $row["rok"];
        $post["us_id"]  = $row["us_id"];
        
        //update our repsonse JSON data
        array_push($response["cars"], $post);
    }
    
    // echoing JSON response
    echo json_encode($response);
    
    
} else {
    $response["success"] = 1;
    $response["message"] = "No Cars In System!";
    die(json_encode($response));
}
} else {
?>

<form action="select_usercars.php" method="post"> 
		<input type="text" name="us_id" value=""; size="30"  /> 
		<br /><br /> 
		<input type="submit" name = "select" value="Select" /> 
</form>
<?php
}

?>