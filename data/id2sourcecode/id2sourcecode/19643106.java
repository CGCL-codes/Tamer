    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        FigureCanvas canvas = (FigureCanvas) viewer.getControl();
        GridData data = new GridData();
        data.horizontalAlignment = GridData.CENTER;
        data.grabExcessHorizontalSpace = true;
        data.verticalAlignment = GridData.CENTER;
        data.grabExcessVerticalSpace = true;
        canvas.setLayoutData(data);
        canvas.setBorder(new LineBorder(1));
        canvas.setSize(720, 576);
        canvas.setVerticalScrollBarVisibility(FigureCanvas.NEVER);
        canvas.setHorizontalScrollBarVisibility(FigureCanvas.NEVER);
        InfoKeyHandler keyHandler = new InfoKeyHandler(viewer, this);
        keyHandler.put(KeyStroke.getPressed(SWT.BS, 8, 0), getActionRegistry().getAction(BackAction.GO_BACK));
        keyHandler.put(KeyStroke.getPressed(SWT.ARROW_LEFT, 0), getActionRegistry().getAction(NavigateAction.LEFT));
        keyHandler.put(KeyStroke.getPressed(SWT.ARROW_RIGHT, 0), getActionRegistry().getAction(NavigateAction.RIGHT));
        keyHandler.put(KeyStroke.getPressed(SWT.ARROW_UP, 0), getActionRegistry().getAction(NavigateAction.UP));
        keyHandler.put(KeyStroke.getPressed(SWT.ARROW_DOWN, 0), getActionRegistry().getAction(NavigateAction.DOWN));
        keyHandler.put(KeyStroke.getPressed(SWT.CR, 13, 0), getActionRegistry().getAction(NavigateAction.OK));
        viewer.setEditPartFactory(new ShapesEditPartFactory());
        viewer.setRootEditPart(new ScalableFreeformRootEditPart());
        viewer.setKeyHandler(keyHandler);
        ContextMenuProvider cmProvider = new ElementEditorContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(cmProvider);
    }
