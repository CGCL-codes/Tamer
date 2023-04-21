    @Override
    protected void showDiagram() {
        if (myVisualizer == null) {
            assert getEditorInput() instanceof WorkflowDesignerEditorInput;
            myRefreshPerformer = new RefreshPerformer(this);
            MenuManager contextMenu = buildContextMenu();
            PaletteRoot palette = buildPalette();
            myVisualizer = new Visualizer(myDiagramPane, myModelProvider, contextMenu, getSite().getPage(), palette);
            getSite().registerContextMenu(contextMenu, myVisualizer.getSelectionProvider());
            mySelectionProvider.setDelegate(myVisualizer.getSelectionProvider());
            myInplaceManager = new WorkflowDesignerInplaceManager(myVisualizer);
            Iterable<Iterable<IAction>> toolbar = buildToolbar((GraphicalViewer) myVisualizer.getViewer());
            myVisualizer.setupActions(toolbar);
        }
        myPageBook.showPage(myDiagramPane);
    }
