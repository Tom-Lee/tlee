var target = UIATarget.localTarget();
//Tap first row
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Test title 3661, Test author"].tap();
target.frontMostApp().mainWindow().staticTexts()["Test title 3661"].tapWithOptions({tapOffset:{x:0.73, y:0.40}});
target.frontMostApp().mainWindow().staticTexts()["Test author"].tapWithOptions({tapOffset:{x:0.82, y:0.62}});
//Tap Book to go back to the book list
target.frontMostApp().navigationBar().leftButton().tap();

//Tap create button
target.frontMostApp().navigationBar().rightButton().tap();
target.frontMostApp().mainWindow().textFields()[0].tap();
target.frontMostApp().keyboard().typeString("test title 574");
target.delay(2);
target.frontMostApp().mainWindow().textFields()[1].tap();
target.frontMostApp().keyboard().typeString("test author 657");
target.frontMostApp().navigationBar().rightButton().tap();

//In the added book detail UI
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["test title 574, test author 657"].tap();
target.frontMostApp().mainWindow().staticTexts()["test title 574"].tapWithOptions({tapOffset:{x:0.73, y:0.40}});
target.frontMostApp().mainWindow().staticTexts()["test author 657"].tapWithOptions({tapOffset:{x:0.82, y:0.62}});

//Tap Book to go back to the book list
target.frontMostApp().navigationBar().leftButton().tap();

//Tap Edit button 
target.frontMostApp().navigationBar().leftButton().tap();
target.delay(2);

//Tap '-' near the added book
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["test title 574, test author 657"].switches()["Delete test title 574, test author 657"].tap();
target.delay(2);
//Tap Delete button 
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["test title 574, test author 657"].buttons()["Confirm Deletion for test title 574, test author 657"].tap();
target.delay(2);
//Tap Done 
target.frontMostApp().navigationBar().leftButton().tap();

//Tap first row
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Test title 3661, Test author"].tap();
target.frontMostApp().mainWindow().staticTexts()["Test title 3661"].tapWithOptions({tapOffset:{x:0.73, y:0.40}});
target.frontMostApp().mainWindow().staticTexts()["Test author"].tapWithOptions({tapOffset:{x:0.82, y:0.62}});

//Tap Book to go back to the book list
target.frontMostApp().navigationBar().leftButton().tap();
