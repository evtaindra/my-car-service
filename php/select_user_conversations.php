<?php 

require("config.inc.php");

if (!empty($_POST)) {
    $query = "Select * FROM users WHERE us_id = :id";
    $query_params = array(
        ':id' => $_POST['us_id']
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
    $rows = $stmt->fetchAll();

    if ($rows) {

        $query = "SELECT DISTINCT S.sr_id, S.name FROM conversations AS C INNER JOIN services AS S ON C.sr_id = S.sr_id WHERE C.us_id=:id";
        $query_params = array(
            ':id' => $_POST['us_id']
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
        $rows = $stmt->fetchAll();

        $response["success"] = 1;
        $response["user_conversations"]   = array();

        foreach ($rows as $row) {
            $user_conversations             = array();
            $user_conversations["sr_id"] =  $row["sr_id"];
            $user_conversations["sr_name"] =  $row["name"];
            array_push($response["user_conversations"], $user_conversations);
        }

        $query = "SELECT sr_id, name FROM services WHERE us_id = :id";
        $query_params = array(
            ':id' => $_POST['us_id']
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
        $services_rows = $stmt->fetchAll();

        $response["user_services_conversations"]   = array();
        foreach ($services_rows as $srow) {
            $user_services_conversations = array();
            $user_services_conversations["sr_id"] =  $srow["sr_id"];
            $user_services_conversations["sr_name"] =  $srow["name"];

            $query = "SELECT DISTINCT U.us_id, U.name, U.surname 
            FROM conversations AS C 
            INNER JOIN users AS U ON C.us_id = U.us_id 
            WHERE C.sr_id=:id";

            $query_params = array(
                ':id' => $srow["sr_id"]
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

            $users_rows = $stmt->fetchAll();
            $user_services_conversations["service_conversation"] = array();
            foreach ($users_rows as $urow) {
                $service_conversation             = array();
                $service_conversation["us_id"] =  $urow["us_id"];
                $service_conversation["fname"] =  $urow["name"];
                $service_conversation["lname"] =  $urow["surname"];
                array_push($user_services_conversations["service_conversation"], $service_conversation);
            }

            array_push($response["user_services_conversations"], $user_services_conversations);
        }  
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "No Users In System!";
        die(json_encode($response));
    }
        

} else {
?>

<form action="select_user_conversations.php" method="post"> 
        <input type="text" name="us_id" value=""; size="30"  /> 
        <br /><br /> 
        <input type="submit" name = "select" value="Select" /> 
</form>
<?php
}

?>