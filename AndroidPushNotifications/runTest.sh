#!/bin/bash
#####
# Purpose: this script is to drive the automation of push notifications' web portal and mobile app
#          to do end to end testing
# Author: tlee 
#####

phone=/Users/ApigeeCorporation/Automation/ADTWorkspace/MainActivityTest
phoneLog=/Users/ApigeeCorporation/Automation/ADTWorkspace/MainActivityTest/logcat.txt
web=/Users/ApigeeCorporation/Automation/EclipseWorkspace/PushNotificationTest
webLog=/Users/ApigeeCorporation/Automation/EclipseWorkspace/PushNotificationTest/log/pushAndroid/test.log
home=`pwd`

function killProcess(){
	count=1
	while [[ $count -gt 0 ]] ; do
		if [[ $1 -eq "adb" ]]
		then
			kill $(ps -ef | grep 'fork-server server' | grep -v 'grep' | cut -d' ' -f 5)
		fi
		for KILLPID in `ps ax | grep $1 | awk ' { print $1;}'`; do
  			kill -9 $KILLPID;
		done
		count=`ps -ef | grep $1 | grep -v 'grep' | wc -l`
	done
}

function restartServer(){
	adb kill-server
	adb start-server
} 

echo 'clear the logs'
echo '' > $phoneLog
killProcess "adb"
killProcess "java"
restartServer
adb logcat -c

sleep 10
deviceId=`adb devices | grep -v 'List' | grep device | cut -f 1`
if [[ -z $deviceId ]] 
then
	killProcess "adb"
	adb kill-server
	echo "Error: the device's serial id was not populated, exit"
	exit 1
fi

echo "The device's serial id is ====> $deviceId"
echo 'Run the web portal push notification test'
cd $web
./runAndroid.sh &

echo "Run the mobile push notification test"
cd $phone
adb -s $deviceId -d install -r bin/MainActivityTest.apk
sleep 5
adb -s $deviceId shell am instrument -e class com.ganyo.pushtest.test.MainActivityTest -w com.ganyo.pushtest.test/android.test.InstrumentationTestRunner 
sleep 65
killProcess "java"
adb logcat -d > $phoneLog
killProcess "adb"

count1=`grep 'automation debug' $phoneLog | grep 'Error' | wc -l`
count2=`grep 'automation debug' $phoneLog | wc -l | cut -d' ' -f -8`
count3=`grep 'Error' $phoneLog | wc -l | cut -d' ' -f -8`

echo ''
echo ''
echo '##################### Test Result Summary #####################'
echo ''
if [[ $count1 -eq 0 && $count2 -gt 1 && $count3 -gt 0 ]]
then 
       	echo ' 1. The Anroid push noticification test passed at the phone'  
else
	echo ' 1. Error: Android push noticification test failed at the phone' 
fi
echo ''
count1=`grep 'Error' $webLog | wc -l | cut -d' ' -f -8`
count2=`wc -l $webLog | cut -d' ' -f -7`
if [[ $count1 -eq 0 && $count2 -gt 1 ]]
then 
       	echo ' 2. The Android push noticification test passed at the web'  
else
       	echo ' 2. Error: The Android push noticification test failed at the web' 
fi
echo ''
echo '###############################################################'
echo ''

cd $home
exit 0
