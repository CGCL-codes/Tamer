    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new CanvasModelEditPartFactory());
        viewer.setContents(getModel());
        viewer.addDropTargetListener(new CanvasDiagramTransferDropTargetListener(viewer));
        viewer.addDropTargetListener(new FileTransferDropTargetListener(viewer));
    }
