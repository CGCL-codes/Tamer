    @Override
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setContents(model);
        getGraphicalViewer().addDropTargetListener((TransferDropTargetListener) new ClassDiagramDropTargetListener(getGraphicalViewer()));
    }
