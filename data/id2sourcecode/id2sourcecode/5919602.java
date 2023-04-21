    @Override
    protected void configureGraphicalViewer() {
        double[] zoomLevels;
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new RecordPartFactory());
        ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
        viewer.setRootEditPart(rootEditPart);
        ZoomManager manager = rootEditPart.getZoomManager();
        getActionRegistry().registerAction(new ZoomInAction(manager));
        getActionRegistry().registerAction(new ZoomOutAction(manager));
        zoomLevels = new double[] { 0.1, 0.25, 0.5, 0.75, 1, 1.5, 2.0, 2.5, 3, 4, 5, 10 };
        manager.setZoomLevels(zoomLevels);
        manager.setZoom(1);
        ArrayList<String> zoomContributions = new ArrayList<String>();
        zoomContributions.add(ZoomManager.FIT_ALL);
        zoomContributions.add(ZoomManager.FIT_HEIGHT);
        zoomContributions.add(ZoomManager.FIT_WIDTH);
        manager.setZoomLevelContributions(zoomContributions);
        keyHandler = new KeyHandler() {

            @Override
            public boolean keyPressed(KeyEvent event) {
                if (event.keyCode == 127) {
                    return performDelete();
                }
                return super.keyPressed(event);
            }

            private boolean performDelete() {
                IAction action = getActionRegistry().getAction(ActionFactory.DELETE.getId());
                if (action == null) return false;
                if (action.isEnabled()) action.run();
                return true;
            }
        };
        ;
        keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), getActionRegistry().getAction(ActionFactory.DELETE.getId()));
        keyHandler.put(KeyStroke.getPressed('+', SWT.KEYPAD_ADD, 0), getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));
        keyHandler.put(KeyStroke.getPressed('-', SWT.KEYPAD_SUBTRACT, 0), getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT));
        viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.NONE), MouseWheelZoomHandler.SINGLETON);
        viewer.setKeyHandler(keyHandler);
        ContextMenuProvider provider = new WhiteboardContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(provider);
    }
