    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        getGraphicalViewer().setContents(getLogicDiagram());
        getGraphicalViewer().addDropTargetListener(new ModelTemplateTransferDropTargetListener(getGraphicalViewer()));
        getGraphicalViewer().addDropTargetListener(new TextTransferDropTargetListener(getGraphicalViewer(), TextTransfer.getInstance()));
    }
