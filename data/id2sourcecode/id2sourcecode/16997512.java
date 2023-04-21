    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = this.getGraphicalViewer();
        viewer.setEditPartFactory(new ProjectEditPartFactory());
        viewer.setRootEditPart(new ScalableFreeformRootEditPart());
        KeyHandler keyHandler = new GraphicalViewerKeyHandler(viewer);
        KeyStroke delKey = KeyStroke.getPressed(SWT.DEL, 127, 0);
        IAction delAction = getActionRegistry().getAction(ActionFactory.DELETE.getId());
        keyHandler.put(delKey, delAction);
        KeyStroke backSpaceKey = KeyStroke.getPressed(SWT.BS, 8, 0);
        IAction backSpaceKeyAction = getActionRegistry().getAction(ActionFactory.DELETE.getId());
        keyHandler.put(backSpaceKey, backSpaceKeyAction);
        viewer.setKeyHandler(keyHandler);
        if (Settings.isMouseMessageOut()) {
            viewer.getControl().addMouseMoveListener(new ZoomedMouseMoveListener(((ScalableFreeformRootEditPart) (viewer.getRootEditPart())).getZoomManager()));
        }
        ;
        ContextMenuProvider provider = new MasePlannerContextMenuProvider(viewer, this.getActionRegistry());
        viewer.setContextMenu(provider);
    }
