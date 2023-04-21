    private void initDragAndDrop(GraphicalViewer viewer) {
        AbstractTransferDragSourceListener dragSourceListener = new ERDiagramTransferDragSourceListener(viewer, TemplateTransfer.getInstance());
        viewer.addDragSourceListener(dragSourceListener);
        AbstractTransferDropTargetListener dropTargetListener = new ERDiagramTransferDropTargetListener(viewer, TemplateTransfer.getInstance());
        viewer.addDropTargetListener(dropTargetListener);
    }
