    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        EditPartViewer viewer = getGraphicalViewer();
        Object objDoc = getEditorInput().getAdapter(Document.class);
        if (objDoc != null) {
            document = (Document) objDoc;
        }
        PaletteBuilder.populatePalette(document.getTemplate(), getPaletteRoot());
        viewer.setContents(getDiagram());
        viewer.addDropTargetListener(createTransferDropTargetListener(getGraphicalViewer()));
        paletteOperator = new PaletteOperator(csdePaletteViewerProvider.getViewer(), PaletteBuilder.getDrawers());
        viewer.addSelectionChangedListener(paletteOperator);
        log.debug("Palette visible: " + getPaletteRoot().isVisible());
        getGraphicalControl().addMouseListener(new MouseAdapter() {

            public void mouseUp(MouseEvent e) {
                if (clickPoint == null) clickPoint = new Point(e.x, e.y); else {
                    clickPoint.x = e.x;
                    clickPoint.y = e.y;
                }
            }
        });
    }
