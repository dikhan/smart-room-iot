#! /bin/bash

[[ -z "$1" ]] &&  echo "Input Error: requires password" && exit -1

password=$1

curl -s -H 'Content-Type: text/xml;charset=utf-8' --data @/tmp/check_room_status --ntlm -u rachocka:$password  https://mail.cisco.com/ews/exchange.asmx
