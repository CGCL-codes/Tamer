package org.put.netbeans.dcs_modeler.actions;

import java.util.logging.Logger;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.put.netbeans.dcs_modeler.dataObjects.DCSComputingObjectDefinitionDataObject;

public final class DCSComputingObjectDefinitionAction extends CookieAction {

    private static Logger log = Logger.getLogger(DCSComputingObjectOpenSuport.class.getName());

    protected void performAction(Node[] activatedNodes) {
        log.info("Created new workflow definition file");
        if (activatedNodes.length != 0) {
            DCSComputingObjectDefinitionDataObject d = (DCSComputingObjectDefinitionDataObject) activatedNodes[0].getLookup().lookup(DCSComputingObjectDefinitionDataObject.class);
            log.info("Created modeler object for " + d.getPrimaryFile().getName());
        }
    }

    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    public String getName() {
        return NbBundle.getMessage(DCSComputingObjectDefinitionAction.class, "CTL_DCSComputingObjectDefinitionAction");
    }

    protected Class[] cookieClasses() {
        return new Class[] { DataObject.class };
    }

    @Override
    protected void initialize() {
        super.initialize();
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
