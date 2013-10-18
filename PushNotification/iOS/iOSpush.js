//The script is to auto-accept two incoming push notification messages and verify them
//@author: tlee

var target = UIATarget.localTarget();
var application = target.frontMostApp();
var window = application.mainWindow();
var startTime = new Date().getTime() / 1000;
var currentTime=0;
var count=0;

target.delay(4);
target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_PORTRAIT);
UIALogger.logStart("Start to test push notification on the iPhone");


function findTime() {
	var now=new Date().getTime() / 1000;
	return now;
}

UIATarget.onAlert = function onAlert(alert) {
		
	if (target.frontMostApp().alert().cancelButton().isValid()){	

		var data=target.frontMostApp().alert().scrollViews()[0].staticTexts()["Text: I just sent another notification"].name();
    		if(data=="Text: I just sent another notification") {
			count+=1;					
			target.frontMostApp().alert().cancelButton().tap();
			UIALogger.logMessage("Found the push notification " + count + " message(s)");
   			UIALogger.logMessage("Click OK button");
			target.delay(2);
			return true;
		}else{
			UIALogger.logFail("Error: mismatch in the received push notification");
			return false;
		}
	}	

    return false;
}

	
while ((currentTime-startTime)<=150){
	if (count==2){
		break;
	}else{
		UIALogger.logMessage("Check to find if there's incoming push notification(s)"); 
		target.frontMostApp().alert().cancelButton().isVisible();
	}
	currentTime=findTime();
}

if (count!=2){
	UIALogger.logError("Error: received incorrect number of push notification(s):"+count);
}else{
	UIALogger.logPass("The push notification test passed on the iPhone");	
}
UIALogger.logMessage("End to test push notification on the iPhone");
