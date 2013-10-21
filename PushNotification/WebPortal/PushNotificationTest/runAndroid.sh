#!/bin/bash

echo 'clean the logs'
echo '' > log/pushAndroid/test.log
echo '' > log/main.log

echo 'remove the previous build'
ant clean
ant
echo 'run Android push notification test'
ant doPushAndroid
grep -v '^$' log/pushAndroid/test.log > temp ; mv temp log/pushAndroid/test.log
