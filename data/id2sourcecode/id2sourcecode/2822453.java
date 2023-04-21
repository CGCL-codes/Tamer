    @Override
    protected void initializeGraphicalViewer() {
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setContents(getQueryDiagram());
        getGraphicalViewer().addDropTargetListener((TransferDropTargetListener) new QueryDropTargetListener(getGraphicalViewer()));
        getGraphicalViewer().addDropTargetListener((TransferDropTargetListener) new TextTransferDropTargetListener(getGraphicalViewer(), TextTransfer.getInstance()));
    }
