    private ZoomManager getZoomManager(GraphicalViewer viewer) {
        RootEditPart rootEditPart = viewer.getRootEditPart();
        ZoomManager zoomManager = null;
        if (rootEditPart instanceof ScalableFreeformRootEditPart) {
            zoomManager = ((ScalableFreeformRootEditPart) rootEditPart).getZoomManager();
        } else if (rootEditPart instanceof ScalableRootEditPart) {
            zoomManager = ((ScalableRootEditPart) rootEditPart).getZoomManager();
        }
        return zoomManager;
    }
