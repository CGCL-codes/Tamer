    public Object getAdapter(Class type) {
        if (type == org.eclipse.ui.views.contentoutline.IContentOutlinePage.class) {
            return outlinePage = createOutlinePage();
        } else if (type.equals(IContentPage.class)) {
            return getContentPage();
        } else if (type == org.eclipse.gef.editparts.ZoomManager.class) {
            return ((ScalableFreeformRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();
        } else if (type == ReportEditorSelectionAdapter.class) {
            return getSelectionAdapter();
        } else if (type == Report.class) {
            return getReport();
        }
        return super.getAdapter(type);
    }
