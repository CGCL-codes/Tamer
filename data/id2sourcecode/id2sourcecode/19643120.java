    protected void initializeGraphicalViewer() {
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setContents(getModel());
        viewer.addDropTargetListener(createTransferDropTargetListener());
    }
