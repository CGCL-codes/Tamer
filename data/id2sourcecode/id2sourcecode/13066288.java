    @Override
    protected void initializeGraphicalViewer() {
        GraphicalViewer viewer = this.getGraphicalViewer();
        viewer.setEditPartFactory(editPartFactory);
        this.initViewerAction(viewer);
        this.initDragAndDrop(viewer);
        viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);
        viewer.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, true);
        viewer.setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, true);
        viewer.setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, true);
        MenuManager menuMgr = new ERDiagramPopupMenuManager(this.getActionRegistry(), this.diagram);
        this.extensionLoader.addERDiagramPopupMenu(menuMgr, this.getActionRegistry());
        viewer.setContextMenu(menuMgr);
        viewer.setContents(diagram);
        this.outlineMenuMgr = new ERDiagramOutlinePopupMenuManager(this.diagram, this.getActionRegistry(), this.outlinePage.getOutlineActionRegistory(), this.outlinePage.getViewer());
        this.gotoMaker = new ERDiagramGotoMarker(this);
    }
