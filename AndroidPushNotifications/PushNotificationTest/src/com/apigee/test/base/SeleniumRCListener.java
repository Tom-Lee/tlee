package com.apigee.test.base;

import org.apache.commons.logging.Log;
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;
import org.testng.ISuite;
import org.testng.ISuiteListener;

/**
 * This class is to define the Selenium listener
 * Author: tlee
 */
public class SeleniumRCListener implements ISuiteListener {

	private SeleniumServer sel;
	protected Log log = TestLogger.getLogger(this.getClass());

	public void onFinish(ISuite suite) {
		if (sel!=null) {
			sel.stop();
			log.info("The selenium RC server stopped....\n");
		}
	}

	public void onStart(ISuite suite) {
	
		String port_param = suite.getParameter("serverport");
		String multiwin_param=suite.getParameter("multiwin");
		//System.out.println(suite.getXmlSuite().getParameters().size());
		try {
			RemoteControlConfiguration configuration = new RemoteControlConfiguration();

			// port
			int port=RemoteControlConfiguration.DEFAULT_PORT;
			if (port_param!=null&&!port_param.isEmpty()) {
				port=Integer.parseInt(port_param);
			}
			configuration.setPort(port);

			// multi window
			boolean multiwin=false;
			if (multiwin_param!=null&&!multiwin_param.isEmpty()){
				multiwin=Boolean.parseBoolean(multiwin_param);
			}
			
			boolean useSingleWindow = false;
			if (multiwin == false){
				useSingleWindow = true;
			}
			configuration.setSingleWindow(useSingleWindow);
			//configuration.setMultiWindow(multiwin);

			SeleniumServer server = new SeleniumServer(false, configuration);
			server.start();

			log.info("The selenium RC server started....\n");
			this.sel=server;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
