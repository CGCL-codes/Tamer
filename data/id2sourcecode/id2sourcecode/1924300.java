    @Override
    @SuppressWarnings("rawtypes")
    public Object getAdapter(Class type) {
        if (type == ZoomManager.class) {
            return ((ScalableFreeformRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();
        } else if (type == IContentOutlinePage.class) {
            outlinePage = new ThumbnailOutlinePage(this);
            return outlinePage;
        } else if (type == IPropertySheetPage.class) {
            return tabbedPropertySheetPage;
        } else {
            return super.getAdapter(type);
        }
    }
