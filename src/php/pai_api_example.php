#!/usr/bin/php
<?php

function getAuthCookie($username, $password) {
    $URL = 'https://www.paireports.com/myreports/Login.event';
    $data = array('Username' => $username, 'Password' => $password);

    $opts = array(
        'http' => array(
            'header' => "Content-Type: application/x-www-form-urlencoded\r\nUser-Agent: PHP-5.5\r\nAccept: application/json\r\n",
            'method' => 'POST',
            'content' => http_build_query($data)
        )
    );

    $ctx = stream_context_create($opts);
    $res = json_decode(file_get_contents($URL, false, $ctx));
    return $res->{'JSESSIONID'};
}

function getNewUserReport($auth_cookie, $username) {
    $URL = 'https://www.paireports.com/myreports/GetNewUserReport.event';

    $opts = array(
        'http' => array(
            'header' => "Content-Type: application/x-www-form-urlencoded\r\nUser-Agent: PHP-5.5\r\nCookie: JSESSIONID=$auth_cookie\r\n",
            'method' => 'POST',
            'content' => "ReportCmd=Filter&ReportCmd=CustomCommand&CustomCmdList=DownloadCSV&F_Username=$username&E_Username=false"
        )
    );

    $ctx = stream_context_create($opts);
    return file_get_contents($URL, false, $ctx);
}

$options = getopt("u:p:f:");
if ( !$options || !array_key_exists("u",$options) || !array_key_exists("p", $options) || !array_key_exists("f",$options)) {
    echo "usage: $argv[0] -u username -p password -f filter_user";
    echo "where username/password are login credentials and filter_user is username to search.";
    exit(1);
}

$auth_cookie = getAuthCookie($options['u'], $options['p']);
print_r(getNewUserReport($auth_cookie, $options['f']));
?>