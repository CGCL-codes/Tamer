    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        System.out.println("NSDiagramEditor.configureGraphicalViewer");
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new NSPartFactory(this));
        viewer.setRootEditPart(new ScalableFreeformRootEditPart());
        GraphicalViewerKeyHandler keyHandler = new GraphicalViewerKeyHandler(viewer);
        keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), getActionRegistry().getAction(ActionFactory.DELETE.getId()));
        viewer.setKeyHandler(keyHandler);
        ContextMenuProvider cmProvider = new NSDiagramEditorContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(cmProvider);
        getSite().registerContextMenu(cmProvider, viewer);
    }
