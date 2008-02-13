/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ******************************************************************************/
package com.idega.app.eplatform;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.idega.app.eplatform.appservermanager.AppserverManager;
import com.idega.app.eplatform.appservermanager.AppservermanagerPlugin;

import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;

/**
 * The application class for the RCP Browser Example. Creates and runs the
 * Workbench, passing a <code>BrowserAdvisor</code> as the workbench advisor.
 * 
 * @issue Couldn't run without initial perspective -- it failed with NPE on
 *        WorkbenchWindow.openPage (called from Workbench.openFirstTimeWindow).
 *        Advisor is currently required to override
 *        getInitialWindowPerspectiveId.
 * 
 * @issue If shortcut bar is hidden, and last view in perspective is closed,
 *        there's no way to get it open again.
 * 
 * @since 3.0
 */
public class BrowserApp implements IApplication {

	/**
	 * Constructs a new <code>BrowserApp</code>.
	 */
	public BrowserApp() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception {
		Display display = PlatformUI.createDisplay();
		try {
			displayStartupMessage(display);
			int code = PlatformUI.createAndRunWorkbench(display, new BrowserAdvisor());
			// exit the application with an appropriate return code
			return code == PlatformUI.RETURN_RESTART ? EXIT_RESTART : EXIT_OK;
			// paintSplash(display);
		}
		finally {
			if (display != null)
				display.dispose();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null)
			return;
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}

	private AppserverManager getManager() {
		return AppservermanagerPlugin.getPlugin().getManager();
	}

	protected boolean isStarted() {
		return getManager().isStarted();
	}

	public void displayStartupMessage(final Display display) {
		final StartupMessageWindow window = new StartupMessageWindow(display);
		window.show();
		window.setMessageText(getManager().getStatus());
		final Thread statusHandlerThread = new Thread() {

			public void run() {
				while (!isStarted()) {
					display.asyncExec(new Runnable() {
						public void run() {
							window.setMessageText(getManager().getStatus());
						}
					});
					try {
						Thread.sleep(1000);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		statusHandlerThread.start();
		 // Set up the event loop.
		while (!isStarted()) {
			if (!display.readAndDispatch()) {
				 // If no more entries in event queue
				display.sleep();
			}
		}
		window.dispose();
		
		//TODO find the right place for this
		openInBrowser();
	}
	
	protected void openInBrowser(){
		edu.stanford.ejalbert.BrowserLauncher launcher;
		try {
			launcher = new edu.stanford.ejalbert.BrowserLauncher();
			launcher.openURLinBrowser("FireFox", "http://localhost:8080/workspace/content/");
	
		} catch (BrowserLaunchingInitializingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedOperatingSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
