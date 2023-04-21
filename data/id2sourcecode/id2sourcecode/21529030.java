    protected void loadProperties() {
        LogicRuler ruler = getLogicDiagram().getRuler(PositionConstants.WEST);
        RulerProvider provider = null;
        if (ruler != null) {
            provider = new LogicRulerProvider(ruler);
        }
        getGraphicalViewer().setProperty(RulerProvider.PROPERTY_VERTICAL_RULER, provider);
        ruler = getLogicDiagram().getRuler(PositionConstants.NORTH);
        provider = null;
        if (ruler != null) {
            provider = new LogicRulerProvider(ruler);
        }
        getGraphicalViewer().setProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER, provider);
        getGraphicalViewer().setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY, new Boolean(getLogicDiagram().getRulerVisibility()));
        getGraphicalViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, new Boolean(getLogicDiagram().isSnapToGeometryEnabled()));
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, new Boolean(getLogicDiagram().isGridEnabled()));
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, new Boolean(getLogicDiagram().isGridEnabled()));
        ZoomManager manager = (ZoomManager) getGraphicalViewer().getProperty(ZoomManager.class.toString());
        if (manager != null) manager.setZoom(getLogicDiagram().getZoom());
        getGraphicalViewer().setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);
    }
