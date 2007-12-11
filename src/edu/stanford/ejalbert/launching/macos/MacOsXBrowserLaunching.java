/************************************************
    Copyright 2005,2006 Olivier Hochreutiner, Jeff Chapman

    This file is part of BrowserLauncher2.

    BrowserLauncher2 is free software; you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    BrowserLauncher2 is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with BrowserLauncher2; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 ************************************************/
// $Id: MacOsXBrowserLaunching.java,v 1.1 2007/12/11 02:35:40 eiki Exp $
package edu.stanford.ejalbert.launching.macos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;

import net.sf.wraplog.AbstractLogger;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.launching.misc.StandardUnixBrowser;
import edu.stanford.ejalbert.launching.misc.UnixNetscapeBrowserLaunching;

/**
 * Launches a browser on MacOS X systems
 * command.
 *
 * @author Eirikur Hrafnsson, Idega Software http://www.idega.com
 */
public class MacOsXBrowserLaunching extends UnixNetscapeBrowserLaunching {
    /**
     * config file for MacOSX.
     */
    public static final String CONFIGFILE_MACOSX =
            "/edu/stanford/ejalbert/launching/macos/MacOSXConfig.properties";

    /**
     * Passes the logger and config file for MacOSX to its
     * super class.
     *
     * @param logger AbstractLogger
     */
    public MacOsXBrowserLaunching(AbstractLogger logger) {
        super(logger, CONFIGFILE_MACOSX);
    }

    /**
     * Uses the which command to find out which browsers are available.
     * The available browsers are put into the unixBrowsers map
     * using displayName => StandardUnixBrowser mappings.
     *
     * @todo what do we do if there are no browsers available?
     * @throws BrowserLaunchingInitializingException
     */
    public void initialize()
            throws BrowserLaunchingInitializingException {
        try {
            URL configUrl = getClass().getResource(configFileName);
            if (configUrl == null) {
                throw new BrowserLaunchingInitializingException(
                        "unable to find config file: " + configFileName);
            }
            StringBuffer potentialBrowserNames = new StringBuffer();
            Properties configProps = new Properties();
            configProps.load(configUrl.openStream());
            String sepChar = configProps.getProperty(PROP_KEY_DELIMITER);
            Iterator keysIter = configProps.keySet().iterator();
            while (keysIter.hasNext()) {
                String key = (String) keysIter.next();
                if (key.startsWith(PROP_KEY_BROWSER_PREFIX)) {
                    StandardUnixBrowser browser = new StandardUnixBrowser(
                            sepChar,
                            configProps.getProperty(key));
                    if (this.isBrowserAvailable(browser,logger)) {
                        unixBrowsers.put(browser.getBrowserDisplayName(),browser);
                    }
                    else {
                        if (potentialBrowserNames.length() > 0) {
                            potentialBrowserNames.append("; ");
                        }
                        potentialBrowserNames.append(
                                browser.getBrowserDisplayName());
                    }
                }
            }
            if (unixBrowsers.size() == 0) {
                // no browser installed
                throw new BrowserLaunchingInitializingException(
                        "one of the supported browsers must be installed: "
                        + potentialBrowserNames);
            }
            logger.info(unixBrowsers.keySet().toString());
            unixBrowsers = Collections.unmodifiableMap(unixBrowsers);
        }
        catch (IOException ioex) {
            throw new BrowserLaunchingInitializingException(ioex);
        }
    }
    
    /**
     * Returns true if the browser is available, ie mdfind command finds it.
     *
     * @param logger AbstractLogger
     * @return boolean
     */
    public boolean isBrowserAvailable(StandardUnixBrowser browser, AbstractLogger logger) {
        boolean isAvailable = false;
        try {
            Process process = Runtime.getRuntime().exec(new String[] {"mdfind",
            		browser.getBrowserApplicationName()});
            InputStream errStream = process.getErrorStream();
            InputStream inStream = process.getInputStream();
            BufferedReader errIn = new BufferedReader(new InputStreamReader(errStream));
            BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
            
            String temp;
            StringBuffer whichOutput = new StringBuffer();
            while ((temp = in.readLine()) != null) {
            	//Searching with spotlight (mdfind) will find more than just the app sometimes
            	if(temp.indexOf(browser.getBrowserApplicationName()) >-1){
            		whichOutput.append(temp);
            	}
            } 
            
            StringBuffer whichErrOutput = new StringBuffer();
            while ((temp = errIn.readLine()) != null) {
            	whichErrOutput.append(temp);
            } 
            
            in.close();
            errIn.close();
            if(whichOutput.length()>0) {
                logger.debug(whichOutput.toString());
                //very weak test but should be enough since the Open -a applicationname works very well
                isAvailable = true;
            }
            if(whichErrOutput.length()>0) {
                logger.debug(whichErrOutput.toString());
            }
        }
        catch (IOException ex) {
            logger.error("io error executing which command", ex);
        }
        return isAvailable;
    }
    
//    /**
//     * Opens a url using the default browser. It uses sdtwebclient
//     * to launch the default browser. The sdtwebclient executable
//     * is mapped to
//     * {@link IBrowserLaunching.BROWSER_DEFAULT IBrowserLaunching.BROWSER_DEFAULT}.
//     *
//     * @param urlString String
//     * @throws BrowserLaunchingExecutionException
//     */
//    public void openUrl(String urlString)
//            throws UnsupportedOperatingSystemException,
//            BrowserLaunchingExecutionException,
//            BrowserLaunchingInitializingException {
//        try {
//            logger.info(urlString);
//            // check system property which may contain user's preferred browser
//            String browserId = System.getProperty(
//                    IBrowserLaunching.BROWSER_SYSTEM_PROPERTY,
//                    null);
//            StandardUnixBrowser defBrowser = getBrowser(
//                    IBrowserLaunching.BROWSER_DEFAULT);
//            if (browserId != null) {
//                logger.info(
//                        "browser pref defined in system prop. Failing over to super.openUrl() method");
//                super.openUrl(urlString);
//            }
//            // we should always have a default browser defined for
//            // SunOS but if not, fail over to super class method
//            else if (defBrowser == null) {
//                logger.info(
//                        "no default browser defined. Failing over to super.openUrl() method");
//                super.openUrl(urlString);
//            }
//            else {
//                logger.info(defBrowser.getBrowserDisplayName());
//                Process process = Runtime.getRuntime().exec(
//                        defBrowser.getArgsForStartingBrowser(urlString));
//                process.waitFor();
//            }
//        }
//        catch (Exception e) {
//            throw new BrowserLaunchingExecutionException(e);
//        }
//    }
}
