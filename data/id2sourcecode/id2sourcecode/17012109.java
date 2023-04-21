    protected void createGraphicalViewer(Composite parent) {
        IEditorSite editorSite = getEditorSite();
        GraphicalViewer viewer = new GraphicalViewerCreator(editorSite).createViewer(parent);
        GraphicalViewerKeyHandler graphicalViewerKeyHandler = new GraphicalViewerKeyHandler(viewer);
        KeyHandler parentKeyHandler = graphicalViewerKeyHandler.setParent(getCommonKeyHandler());
        viewer.setKeyHandler(parentKeyHandler);
        getEditDomain().addViewer(viewer);
        getSite().setSelectionProvider(viewer);
        viewer.setContents(schema);
        ContextMenuProvider provider = new DefaultContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(provider);
        getSite().registerContextMenu("net.confex.schema.editor.contextmenu", provider, viewer);
        this.graphicalViewer = viewer;
    }
