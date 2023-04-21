    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        final ScrollingGraphicalViewer scrollingGraphicalViewer = (ScrollingGraphicalViewer) getGraphicalViewer();
        ScalableFreeformRootEditPart root = new J2DScalableFreeformRootEditPart();
        scrollingGraphicalViewer.setRootEditPart(root);
        ZoomManager zoomManager = root.getZoomManager();
        ArrayList arraylist = new ArrayList(3);
        arraylist.add(ZoomManager.FIT_ALL);
        arraylist.add(ZoomManager.FIT_WIDTH);
        arraylist.add(ZoomManager.FIT_HEIGHT);
        zoomManager.setZoomLevelContributions(arraylist);
        double ad[] = { 0.01D, 0.10000000000000001D, 0.25D, 0.5D, 0.75D, 1.0D, 1.5D, 2D, 2.5D, 3D, 4D };
        zoomManager.setZoomLevels(ad);
        ReportContextMenuProvider erdcontextmenuprovider = new ReportContextMenuProvider(getGraphicalViewer(), getActionRegistry(), this);
        getGraphicalViewer().setContextMenu(erdcontextmenuprovider);
        ZoomInAction zoomInAction = new ZoomInAction(root.getZoomManager());
        ZoomOutAction zoomOutAction = new ZoomOutAction(root.getZoomManager());
        getActionRegistry().registerAction(zoomInAction);
        getActionRegistry().registerAction(zoomOutAction);
        getSite().getKeyBindingService().registerAction(zoomInAction);
        getSite().getKeyBindingService().registerAction(zoomOutAction);
        IAction action = null;
        action = new ToggleGridVisibilityAction(this, getGraphicalViewer());
        getActionRegistry().registerAction(action);
        action = new ToggleSnapToGridAction(this, getGraphicalViewer());
        getActionRegistry().registerAction(action);
        IPreferenceStore store = ReportDesignerPlugin.getDefault().getPreferenceStore();
        getGraphicalViewer().getControl().setBackground(ReportDesignerConstants.DEFAULT_EDITOR_BACKGROUND);
        getGraphicalViewer().addDropTargetListener((TransferDropTargetListener) new DropExpressionListener(getGraphicalViewer()));
        getGraphicalViewer().setProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER, new HorizontalRulerProvider());
        getGraphicalViewer().setProperty(RulerProvider.PROPERTY_VERTICAL_RULER, new VerticalRulerProvider());
        getGraphicalViewer().setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY, store.getBoolean(ReportDesignerConstants.RULERS_VISIBLE_PREF));
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, store.getBoolean(ReportDesignerConstants.GRID_VISIBLE_PREF));
        getGraphicalViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, store.getBoolean(ReportDesignerConstants.SNAP_TO_GRID_PREF));
        int gridSpacing = store.getInt(ReportDesignerConstants.GRID_SPACING_PREF);
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension(gridSpacing, gridSpacing));
        getGraphicalViewer().setEditPartFactory(new ReportPartFactory());
        getGraphicalViewer().setKeyHandler((new GraphicalViewerKeyHandler(scrollingGraphicalViewer)).setParent(getCommonKeyHandler()));
        zoomManager.addZoomListener(new ZoomListener() {

            public void zoomChanged(double d) {
                Composite parent = scrollingGraphicalViewer.getControl().getParent();
                parent.layout();
            }
        });
    }
