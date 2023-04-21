    @SuppressWarnings("unchecked")
    public Object getAdapter(Class type) {
        if (type == ZoomManager.class) {
            return ((ScalableRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();
        }
        return super.getAdapter(type);
    }
