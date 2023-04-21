    protected void initializeGraphicalViewer() {
        GraphicalViewer graphicalViewer = getGraphicalViewer();
        graphicalViewer.setContents(diagram);
        graphicalViewer.addDropTargetListener(new TableDropTargetListener(graphicalViewer));
    }
