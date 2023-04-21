package org.dbe.toolkit.portal.service.ui;

import org.dbe.toolkit.proxyframework.Workspace;
import org.dbe.toolkit.proxyframework.ui.AbstractServiceUI;

/**
 * 
 * @author bob 
 */
public class DBEPortalUI extends AbstractServiceUI {

    Workspace ws;

    /**
     * @param arg0
     */
    public DBEPortalUI(Workspace arg0) {
        super(arg0);
    }

    /**
     * @see org.dbe.toolkit.proxyframework.ui.ServiceUI#startUI()
     */
    public String startUI() {
        return "http://";
    }
}
