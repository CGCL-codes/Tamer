    @Override
    public Object getAdapter(Class type) {
        if (type == ZoomManager.class) {
            return ((ScalableFreeformRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();
        }
        if (type == IContentOutlinePage.class) {
            return this.outlinePage;
        }
        if (type == IGotoMarker.class) {
            return this.gotoMaker;
        }
        if (type == IPropertySheetPage.class) {
            return this.propertySheetPage;
        }
        return super.getAdapter(type);
    }
