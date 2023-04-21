    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new ArchimateDiagramEditPartFactory());
        viewer.setContents(getModel());
        viewer.addDropTargetListener(new ArchimateDiagramTransferDropTargetListener(viewer));
    }
