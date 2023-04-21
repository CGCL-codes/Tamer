    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        System.out.println("NSDiagramEditor.getPaletteRoot");
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setContents(DrawCodePlugin.getDefault().getActiveClassDiagram().root);
        viewer.addDropTargetListener(createTransferDropTargetListener());
    }
