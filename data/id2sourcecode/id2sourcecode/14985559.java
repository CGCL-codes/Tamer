    public Object getAdapter(Class type) {
        if (type == org.eclipse.gef.ui.stackview.CommandStackInspectorPage.class) {
            return new CommandStackInspectorPage(getCommandStack());
        }
        if (type == org.eclipse.ui.views.contentoutline.IContentOutlinePage.class) {
            return outlinePage = new OutlinePage(new TreeViewer());
        }
        if (type == IContentOverviewPage.class) {
            return new OverviewPage(new TreeViewer());
        }
        if (type == org.eclipse.gef.editparts.ZoomManager.class) {
            return ((ScalableFreeformRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();
        } else {
            return super.getAdapter(type);
        }
    }
