package de.beas.explicanto.client.rcp.projects.actions;

import org.eclipse.jface.action.IAction;
import de.bea.services.vidya.client.datastructures.CCustomer;
import de.beas.explicanto.client.rcp.actions.GenericObjectAction;
import de.beas.explicanto.client.rcp.dialogs.ExplicantoMessageDialog;
import de.beas.explicanto.client.rcp.model.Model;

/**
 * 
 * The new project action obejct action
 *
 * @author alexadru.georgescu
 * @version 1.0
 *
 */
public class NewProjectAction extends GenericObjectAction {

    private CCustomer customer = null;

    public void run(IAction action) {
        ExplicantoMessageDialog.openForbidenActionDlg(window.getShell());
    }

    public void setNewSelection(Object selectedNode) {
        customer = (CCustomer) selectedNode;
    }

    protected String getTranslatedName() {
        return translate("actions.newProject.title");
    }

    protected String getTranslatedTooltip() {
        return translate("actions.newProject.tooltip");
    }
}
