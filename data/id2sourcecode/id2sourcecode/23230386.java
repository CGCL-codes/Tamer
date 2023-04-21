    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        getGraphicalViewer().setRootEditPart(new ScalableRootEditPart());
        getGraphicalViewer().setEditPartFactory(new FSMStatePartFactory());
        getGraphicalViewer().setKeyHandler(new GraphicalViewerKeyHandler(getGraphicalViewer()).setParent(getCommonKeyHandler()));
        ContextMenuProvider provider = new FSMContextMenuProvider(getGraphicalViewer(), getActionRegistry());
        getGraphicalViewer().setContextMenu(provider);
        getSite().registerContextMenu("org.eclipse.gef.examples.flow.editor.contextmenu", provider, getGraphicalViewer());
    }
