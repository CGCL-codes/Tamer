    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
        viewer.setRootEditPart(rootEditPart);
        viewer.setEditPartFactory(new GraphicalPartFactory());
        configureZoomManager(rootEditPart);
        getEditDomain().addViewer(viewer);
        this.synchronizer.addViewer(viewer);
        getSite().setSelectionProvider(viewer);
        ContextMenuProvider cmProvider = new BpmoEditorContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(cmProvider);
        getSite().registerContextMenu(cmProvider, viewer);
        ((FigureCanvas) viewer.getControl()).setScrollBarVisibility(FigureCanvas.ALWAYS);
    }
