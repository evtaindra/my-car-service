<?php

require("config.inc.php");

if (!empty($_POST)) {

    if (empty($_POST['us_id']) || 
        empty($_POST['sr_id']) || 
        empty($_POST['sender']) || 
        empty($_POST['message'])) {

        $response["success"] = 0;
        $response["message"] = "Please enter all required fields.";
        die(json_encode($response));
    }
    

    $query = "INSERT INTO conversations ( us_id,sr_id, sender,message,attach,isread) 
    VALUES ( :us_id, :sr_id, :sender, :message, :attach, :isread) ";

    $query_params = array(
        ':us_id' => $_POST['us_id'],
        ':sr_id' => $_POST['sr_id'],
        ':sender' => $_POST['sender'],
        ':message' => $_POST['message'],
        ':attach' => $_POST['attachment'],
        ':isread' => 0
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
    $response["message"] = "Message Successfully Sent!";

    echo json_encode($response);  
    
} else {
?>
	<form action="send_message.php" method="post"> 
	    <input type="text" name="us_id" size="30" maxlength = "30" value="" /> 
	    <br /><br /> 
        <input type="text" name="sr_id" size="30" maxlength = "30" value="" /> 
        <br /><br /> 
	    <input type="text" name="sender" size="10" maxlength = "30" value="" /> 
        <br /><br /> 
	    <input type="text" name="message" size="30" value="" /> 
		<br /><br /> 
	    <input type="text" name="attachment" size="30" value="" /> 
		<br /><br />     

        <br /><br /> 
        <input type="submit" value="Send" /> 
	</form>
	<?php
}

?>