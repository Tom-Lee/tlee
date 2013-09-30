var target = UIATarget.localTarget();
var appWindow = target.frontMostApp().mainWindow();
target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_PORTRAIT);
UIALogger.logStart("Start to test books app");
UIALogger.logMessage("Tap first row");
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Test title 9158, Test author"].tap();
target.frontMostApp().mainWindow().staticTexts()["Test title 9158"].tapWithOptions({tapOffset:{x:0.73, y:0.40}});
target.frontMostApp().mainWindow().staticTexts()["Test author"].tapWithOptions({tapOffset:{x:0.82, y:0.62}});
UIALogger.logMessage("Tap Book to go back to the book list");
target.frontMostApp().navigationBar().leftButton().tap();


UIALogger.logMessage("Tap create button");
target.frontMostApp().navigationBar().rightButton().tap();
appWindow.textFields()[0].tap();
target.frontMostApp().keyboard().typeString("test title 574");
target.delay(2);
appWindow.textFields()[1].tap();
target.frontMostApp().keyboard().typeString("test author 657");
target.frontMostApp().navigationBar().rightButton().tap();

UIALogger.logMessage("In the added book detail UI");
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["test title 574, test author 657"].tap();
target.frontMostApp().mainWindow().staticTexts()["test title 574"].tapWithOptions({tapOffset:{x:0.73, y:0.40}});
target.frontMostApp().mainWindow().staticTexts()["test author 657"].tapWithOptions({tapOffset:{x:0.82, y:0.62}});
UIALogger.logMessage("Tap Book to go back to the book list");
target.frontMostApp().navigationBar().leftButton().tap();


UIALogger.logMessage("Tap Edit button");
target.frontMostApp().navigationBar().leftButton().tap();
target.delay(2);

UIALogger.logMessage("Tap '-' near the added book");
appWindow.tableViews()["Empty list"].cells()["test title 574, test author 657"].switches()["Delete test title 574, test author 657"].tap();
target.delay(2);
UIALogger.logMessage("Tap Delete button"); 
appWindow.tableViews()["Empty list"].cells()["test title 574, test author 657"].buttons()["Confirm Deletion for test title 574, test author 657"].tap();
target.delay(2);
UIALogger.logMessage("Tap Done"); 
target.frontMostApp().navigationBar().leftButton().tap();

UIALogger.logMessage("Tap first row");
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Test title 9158, Test author"].tap();
target.frontMostApp().mainWindow().staticTexts()["Test title 9158"].tapWithOptions({tapOffset:{x:0.73, y:0.40}});
target.frontMostApp().mainWindow().staticTexts()["Test author"].tapWithOptions({tapOffset:{x:0.82, y:0.62}});
UIALogger.logMessage("Tap Book to go back to the book list");
target.frontMostApp().navigationBar().leftButton().tap();

UIALogger.logPass("The test passed");

