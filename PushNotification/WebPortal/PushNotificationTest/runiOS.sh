#!/bin/bash

echo 'clean the logs'
echo '' > log/pushiOS/test.log 
echo '' > log/main.log

echo 'remove the previous build'
ant clean
ant
echo 'run iOS push notification test'
ant doPushiOS
grep -v '^$' log/pushiOS/test.log > temp ; mv temp log/pushiOS/test.log
