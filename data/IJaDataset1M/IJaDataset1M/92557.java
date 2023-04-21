package de.beas.explicanto.client.rcp.workspace.commands;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import de.beas.explicanto.client.rcp.workspace.views.ProjectsOutline;

public abstract class GenericCommand {

    private IWorkbenchPage page = null;

    public GenericCommand(IWorkbenchPage page) {
        if (page == null) this.page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(); else this.page = page;
    }

    public abstract void run();

    protected IWorkbenchWindow getWindow() {
        return page.getWorkbenchWindow();
    }

    protected IViewPart getView(String viewId) {
        return page.findView(viewId);
    }

    protected ProjectsOutline getOutline() {
        return (ProjectsOutline) getView(ProjectsOutline.ID);
    }
}
