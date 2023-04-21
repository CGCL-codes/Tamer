    @Override
    public Object getAdapter(@SuppressWarnings("rawtypes") Class type) {
        if (type == ZoomManager.class) return ((ScalableFreeformRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();
        return super.getAdapter(type);
    }
