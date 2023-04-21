    protected void initializeGraphicalViewer() {
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setContents(diagram);
        viewer.addDropTargetListener(new MyTemplateTransferDropTargetListener(viewer));
    }
