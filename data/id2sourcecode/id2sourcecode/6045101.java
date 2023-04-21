    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        getGraphicalViewer().setContents(getLogicDiagram());
        getGraphicalViewer().addDropTargetListener((TransferDropTargetListener) new TemplateTransferDropTargetListener(getGraphicalViewer()));
        getGraphicalViewer().addDropTargetListener((TransferDropTargetListener) new TextTransferDropTargetListener(getGraphicalViewer(), TextTransfer.getInstance()));
    }
