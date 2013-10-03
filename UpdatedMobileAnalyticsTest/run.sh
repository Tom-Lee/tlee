#!/bin/bash

## clean the logs
echo '' > log/loginlogout/test.log
echo '' > log/mobileAnalytics/test.log

# remove the previous build
ant clean

# run loginlogout test 
ant doLoginLogout

# run mobile analytics test
ant doMobileAnalytics
