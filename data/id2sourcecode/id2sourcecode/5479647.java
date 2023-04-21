    protected void saveProperties() {
        getLogicDiagram().setRulerVisibility(((Boolean) getGraphicalViewer().getProperty(RulerProvider.PROPERTY_RULER_VISIBILITY)).booleanValue());
        getLogicDiagram().setGridEnabled(((Boolean) getGraphicalViewer().getProperty(SnapToGrid.PROPERTY_GRID_ENABLED)).booleanValue());
        getLogicDiagram().setSnapToGeometry(((Boolean) getGraphicalViewer().getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED)).booleanValue());
        ZoomManager manager = (ZoomManager) getGraphicalViewer().getProperty(ZoomManager.class.toString());
        if (manager != null) getLogicDiagram().setZoom(manager.getZoom());
    }
