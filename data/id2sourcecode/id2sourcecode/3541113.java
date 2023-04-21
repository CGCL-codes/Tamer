    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        final GraphicalViewer viewer = this.getGraphicalViewer();
        viewer.setEditPartFactory(new AssistantEditPartFactory(new AssistantFigureFactory()));
        viewer.setRootEditPart(new VGAPScalableFreeformRootEditPart());
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
        this.configureContextMenu(viewer);
    }
