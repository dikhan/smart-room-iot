#! /bin/bash

[[ -z "$1" ]] &&  echo "Input Error: requires user" && exit -1
[[ -z "$2" ]] &&  echo "Input Error: requires password" && exit -1
[[ -z "$3" ]] &&  echo "Input Error: requires post data file" && exit -1

user=$1
password=$2
postDataFile=$3

curl -s -H 'Content-Type: text/xml;charset=utf-8' --data @$postDataFile --ntlm -u $user:$password  \
https://mail.cisco.com/ews/exchange.asmx
