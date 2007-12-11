package com.idega.app.eplatform;

import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;

public class BrowserLauncherTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		edu.stanford.ejalbert.BrowserLauncher launcher;
		try {
			launcher = new edu.stanford.ejalbert.BrowserLauncher();
			launcher.openURLinBrowser("WebKit", "http://apache.org");

		} catch (BrowserLaunchingInitializingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedOperatingSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
