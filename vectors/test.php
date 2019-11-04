<?php

$cFile = '@some_file.png';
$post = array('image'=> $cFile);
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, 'https://repository2.3dsource.com/acp/?action=upload_file&ts=2234234&host=host');
curl_setopt($ch, CURLOPT_POST,1);
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);
curl_setopt($ch, CURLOPT_POSTFIELDS, $post);
echo curl_getinfo($ch);
