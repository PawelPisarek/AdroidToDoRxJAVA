<?php

/* Send a string after a random number of seconds (2-10) */

$email = $_GET['email'];
$data='';
//echo $email;
header('Content-Type: application/json');
do {
    $data2 = file_get_contents('http://localhost:2000/task-new?email='.$email);
    $data = json_decode($data2, true)['email'];
//    echo $data;

} while($data=="brak nowych");
echo $data2;