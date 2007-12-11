/************************************************
    Copyright 2004,2006 Markus Gebhard, Jeff Chapman

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
// $Id: UnsupportedOperatingSystemException.java,v 1.1 2007/12/11 02:35:41 eiki Exp $
package edu.stanford.ejalbert.exception;

/**
 * Exception thrown when the Operating System is not supported by
 * the browser launcher project.
 *
 * @author Markus Gebhard
 */
public class UnsupportedOperatingSystemException
        extends Exception {

    public UnsupportedOperatingSystemException(String message) {
        super(message);
    }

    public UnsupportedOperatingSystemException(Throwable cause) {
        super(cause);
    }
}
