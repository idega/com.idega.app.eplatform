/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.idega.app.eplatform;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import com.idega.app.eplatform.appservermanager.AppservermanagerPlugin;

/**
 * Preference initializer for the browser preferences.
 */
public class BrowserPreferenceInitializer extends AbstractPreferenceInitializer {

    public BrowserPreferenceInitializer() {
        // do nothing
    }

    /* (non-Javadoc)
     */
    public void initializeDefaultPreferences() {
        Preferences prefs = BrowserPlugin.getDefault().getPluginPreferences();
        //prefs.setDefault(IBrowserConstants.PREF_HOME_PAGE, "http://eclipse.org");  //$NON-NLS-1$
        //prefs.setDefault(IBrowserConstants.PREF_HOME_PAGE, "http://localhost:8080/content");  //$NON-NLS-1$
        prefs.setDefault(IBrowserConstants.PREF_HOME_PAGE, AppservermanagerPlugin.getPlugin().getManager().getMainWebapp().getMainAppURL());
    }

}
