    protected void createGraphicalViewer(Composite parent) {
        IEditorSite editorSite = getEditorSite();
        GraphicalViewer viewer = new GraphicalViewerCreator(editorSite).createViewer(parent);
        GraphicalViewerKeyHandler graphicalViewerKeyHandler = new GraphicalViewerKeyHandler(viewer);
        KeyHandler parentKeyHandler = graphicalViewerKeyHandler.setParent(getCommonKeyHandler());
        viewer.setKeyHandler(parentKeyHandler);
        getEditDomain().addViewer(viewer);
        getSite().setSelectionProvider(viewer);
        viewer.setContents(mapping);
        ContextMenuProvider provider = new MappingContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(provider);
        DataEditDropTargetListener dropListener = new DataEditDropTargetListener(viewer);
        DropTarget dt = new DropTarget(viewer.getControl(), DND.DROP_COPY);
        Transfer transfer = dropListener.getTransfer();
        dt.setTransfer(new Transfer[] { transfer });
        dt.addDropListener(dropListener);
        JavaCore.addElementChangedListener(new MyJavaElementChangeReporter());
        this.graphicalViewer = viewer;
    }
