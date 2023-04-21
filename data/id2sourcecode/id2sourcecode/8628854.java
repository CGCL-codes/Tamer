    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new SketchEditPartFactory());
        viewer.setContents(getModel());
        viewer.addDropTargetListener(new SketchDiagramTransferDropTargetListener(viewer));
    }
