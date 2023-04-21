package com.tensegrity.palorcp.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.views.IViewDescriptor;

/**
 * <code>ShowViewAction</code>
 * 
 * <p>
 * An own implementation of the show view menu action which is known from the
 * eclipse ide (Window -> ShowView) but not present within a rcp.
 * </p>
 * 
 * @author Stepan Rutz
 * @version $Id$
 */
public class ShowViewAction extends Action implements IWorkbenchWindowActionDelegate {

    private IViewDescriptor desc;

    private IWorkbenchWindow window;

    public ShowViewAction(IViewDescriptor desc) {
        if (desc == null) throw new IllegalArgumentException("dedsc must not be null!");
        this.desc = desc;
        this.setText(desc.getLabel());
        this.setToolTipText(desc.getLabel());
        this.setId(getClass().getName() + "." + desc.getId());
        this.setActionDefinitionId(getClass().getName() + "." + desc.getId());
        if (desc.getImageDescriptor() != null) {
            this.setImageDescriptor(desc.getImageDescriptor());
        }
    }

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    public void run() {
        doRun();
    }

    public void run(IAction action) {
        doRun();
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

    private void doRun() {
        IWorkbenchPage page = window.getActivePage();
        if (page == null) return;
        if (desc == null) return;
        try {
            page.showView(desc.getId());
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }
}
