    protected void initializeGraphicalViewer() {
        getGraphicalViewer().setContents(diagram);
        getGraphicalViewer().addDropTargetListener(new BMTransferDropTargetListener(getGraphicalViewer()));
    }
