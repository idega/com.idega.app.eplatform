/************************************************
    Copyright 2004,2005,2006,2007 Markus Gebhard, Jeff Chapman

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
// $Id: BrowserLaunchingFactory.java,v 1.1 2007/12/11 02:35:41 eiki Exp $
package edu.stanford.ejalbert.launching;

import net.sf.wraplog.AbstractLogger;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import edu.stanford.ejalbert.launching.macos.MacOs2_0BrowserLaunching;
import edu.stanford.ejalbert.launching.macos.MacOs2_1BrowserLaunching;
import edu.stanford.ejalbert.launching.macos.MacOs3_0BrowserLaunching;
import edu.stanford.ejalbert.launching.macos.MacOs3_1BrowserLaunching;
import edu.stanford.ejalbert.launching.macos.MacOsXBrowserLaunching;
import edu.stanford.ejalbert.launching.misc.SunOSBrowserLaunching;
import edu.stanford.ejalbert.launching.misc.UnixNetscapeBrowserLaunching;
import edu.stanford.ejalbert.launching.windows.WindowsBrowserLaunching;

/**
 * Factory for determining the OS and returning the appropriate version
 * of IBrowserLaunching. The factory uses
 * {@link System#getProperty(String) System.getProperty("os.name")} to
 * determine the OS.
 *
 * @author Markus Gebhard
 */
public class BrowserLaunchingFactory {

    /**
     * Analyzes the name of the underlying operating system
     * based on the "os.name" system property and returns
     * the IBrowserLaunching version appropriate for the
     * O/S.
     *
     * @param logger AbstractLogger
     * @return IBrowserLaunching
     * @throws UnsupportedOperatingSystemException
     */
    public static IBrowserLaunching createSystemBrowserLaunching(
            AbstractLogger logger)
            throws UnsupportedOperatingSystemException {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Mac OS")) {
            logger.info("Mac OS");
            if(osName.equals("Mac OS X")){
            	return new MacOsXBrowserLaunching(logger);
            }
            else{
            	//older macos
	            String mrjVersion = System.getProperty("mrj.version");
	            String majorMRJVersion = mrjVersion.substring(0, 3);
	            try {
	                double version = Double.valueOf(majorMRJVersion).doubleValue();
	                logger.info("version=" + Double.toString(version));
	                if (version == 2) {
	                    return new MacOs2_0BrowserLaunching();
	                }
	                else if (version >= 2.1 && version < 3) {
	                    // Assume that all 2.x versions of MRJ work the same.  MRJ 2.1 actually
	                    // works via Runtime.exec() and 2.2 supports that but has an openURL() method
	                    // as well that we currently ignore.
	                    return new MacOs2_1BrowserLaunching();
	                }
	                else if (version == 3.0) {
	                    return new MacOs3_0BrowserLaunching();
	                }
	                else if (version >= 3.1) {
	                    // Assume that all 3.1 and later versions of MRJ work the same.
	                    return new MacOs3_1BrowserLaunching();
	                }
	                else {
	                    throw new UnsupportedOperatingSystemException(
	                            "Unsupported MRJ version: " + version);
	                }
	            }
	            catch (NumberFormatException nfe) {
	                throw new UnsupportedOperatingSystemException(
	                        "Invalid MRJ version: " + mrjVersion);
	            }
            }
        }
        else if (osName.startsWith("Windows")) {
            logger.info("Windows OS");
            if (osName.indexOf("9") != -1 ||
                osName.indexOf("Windows Me") != -1) {
                return new WindowsBrowserLaunching(
                        logger,
                        WindowsBrowserLaunching.WINKEY_WIN9X);
            }
            else if(osName.indexOf("Vista") != -1) {
                return new WindowsBrowserLaunching(
                        logger,
                        WindowsBrowserLaunching.WINKEY_WINVISTA);
            }
            else if (osName.indexOf("2000") != -1 ||
                     osName.indexOf("XP") != -1) {
                return new WindowsBrowserLaunching(
                        logger,
                        WindowsBrowserLaunching.WINKEY_WIN2000);
            }
            else {
                return new WindowsBrowserLaunching(
                        logger,
                        WindowsBrowserLaunching.WINKEY_WINNT);
            }
        }
        else if (osName.startsWith("SunOS")) {
            logger.info("SunOS");
            return new SunOSBrowserLaunching(logger);
        }
        else {
            logger.info("Unix-type OS");
            return new UnixNetscapeBrowserLaunching(
                    logger,
                    UnixNetscapeBrowserLaunching.CONFIGFILE_LINUX_UNIX);
        }
    }
}
