    @SuppressWarnings("rawtypes")
    public Object getAdapter(Class type) {
        if (type == ZoomManager.class) return ((ScalableFreeformRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();
        if (type == IContentOutlinePage.class) {
            return new OutlinePage();
        }
        return super.getAdapter(type);
    }
