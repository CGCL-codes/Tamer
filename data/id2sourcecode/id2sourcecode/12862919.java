    @Override
    protected void initializeGraphicalViewer() {
        getGraphicalViewer().setContents(this.diagram);
        getGraphicalViewer().addDropTargetListener(new DiagramTemplateTransferDropTargetListener(getGraphicalViewer()));
    }
