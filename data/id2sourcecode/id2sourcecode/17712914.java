    private void createGraphicalViewer() {
        graphicalViewer = new ProlixScrollingGraphicalViewer(this);
        graphicalViewer.createControl(paletteContainer);
        parent.getEditDomain().addViewer(graphicalViewer);
        graphicalViewer.getControl().setBackground(LDT_Constrains.ColorDiagramBackground);
        graphicalViewer.setRootEditPart(new ScalableFreeformRootEditPart());
        graphicalViewer.setKeyHandler(new GraphicalViewerKeyHandler(graphicalViewer));
        parent.getSite().setSelectionProvider(graphicalViewer);
        graphicalViewer.setEditPartFactory(new ModelEditPartFactory());
        ContextMenuProvider cmProvider = new EditorContextMenuProvider(graphicalViewer, parent.getActionRegistry());
        graphicalViewer.setContextMenu(cmProvider);
        parent.getSite().registerContextMenu(cmProvider, graphicalViewer);
        initGraphicalViewer();
    }
