    @Override
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        getGraphicalViewer().setContents(databaseDiagram);
        getGraphicalViewer().addDropTargetListener(new GEFEditorDropTargetListener(this));
    }
