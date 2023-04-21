    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        getGraphicalViewer().setRootEditPart(new ScalableRootEditPart());
        getGraphicalViewer().setEditPartFactory(new AuroraPartFactory(editorMode));
        getGraphicalViewer().setKeyHandler(new GraphicalViewerKeyHandler(getGraphicalViewer()).setParent(getCommonKeyHandler()));
        ContextMenuProvider provider = new ViewContextMenuProvider(getGraphicalViewer(), getActionRegistry());
        getGraphicalViewer().setContextMenu(provider);
        getSite().registerContextMenu(CONTEXT_MENU_KEY, provider, getGraphicalViewer());
        getGraphicalViewer().addSelectionChangedListener(propertyViewer);
    }
