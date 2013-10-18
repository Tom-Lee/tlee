#!/bin/bash

#####
# Purpose: this script is to run the automation of iOS push notifications' web portal and mobile app
#          to do end to end testing
# Author: tlee 
#####

mobileId=278f4a3436c3d2b2bcd75845e760f7311ce0bfe9
web=/Users/ApigeeCorporation/Automation/EclipseWorkspace/PushNotificationTest
script=/Users/ApigeeCorporation/Automation/ADTWorkspace/iOSPushNotification/iOSpush.js
xcode=/Applications/Xcode.app/Contents/Applications/Instruments.app/Contents/PlugIns/AutomationInstrument.bundle/Contents/Resources/Automation.tracetemplate
webLog=/Users/ApigeeCorporation/Automation/EclipseWorkspace/PushNotificationTest/log/pushiOS/test.log
home=`pwd`

function killProcess(){
        count=1
        while [[ $count -gt 0 ]] ; do
                for KILLPID in `ps ax | grep $1 | awk ' { print $1;}'`; do
                        kill -9 $KILLPID;
                done
                count=`ps -ef | grep $1 | grep -v 'grep' | wc -l`
        done
}

echo 'remove previous logs'
rm -rf instrumentscli0.trace
rm -rf Run*
rm -f ./test.log
killProcess "instruments"
killProcess "java"

echo "Run the mobile push notification test"
instruments -w $mobileId -t $xcode /Users/ApigeeCorporation/Automation/ADTWorkspace/apigee-ios-sdk-master/samples/push/Push\ Test.xcodeproj -e UIASCRIPT $script -e UIARESULTSPATH "./" >> ./test.log &

echo 'Run the web portal push notification test'
cd $web
./runiOS.sh
sleep 110

echo "Terminate any running processes"
killProcess "instruments"
killProcess "java"

cd $home
count1=`echo $(grep 'Error' ./test.log | wc -l| cut -d' ' -f -8)`
count2=`echo $(wc -l ./test.log | cut -d' ' -f -8)`
echo ''
echo ''
echo '##################### Test Result Summary #####################'
echo ''
if [[ $count1 -eq 0 && $count2 -gt 2 ]]
then
        echo '   1. The iOS push notification test passed at the phone'  
else
        echo '   1. Error: the iOS push notification test failed at the phone' 
fi
echo ''
count1=`echo $(grep 'Error' $webLog | wc -l |cut -d' ' -f -8)`
count2=`echo $(wc -l $webLog | cut -d' ' -f -7)`
if [[ $count1 -eq 0 && $count2 -gt 20 ]]
then
        echo '   2. The iOS push notification test passed at the web'  
else
        echo '   2. Error: the iOS push notification test failed at the web' 
fi
echo ''
echo '###############################################################'
echo ''

exit 0
