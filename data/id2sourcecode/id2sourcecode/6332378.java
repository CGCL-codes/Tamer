    protected void createToolBarActions(CoolBar toolBar) {
        if (toolBar != null) {
            CoolBarManager coolBarManager = new CoolBarManager(toolBar);
            ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT | SWT.WRAP | SWT.HORIZONTAL);
            ZoomManager zoomManager = (ZoomManager) getAdapter(ZoomManager.class);
            if (zoomManager != null) {
                IAction zoomIn = new ZoomInAction(zoomManager);
                toolBarManager.add(zoomIn);
                IAction zoomOut = new ZoomOutAction(zoomManager);
                toolBarManager.add(zoomOut);
                IAction zoomOriginal = new ZoomOriginalAction(this, getGraphicalViewer());
                toolBarManager.add(zoomOriginal);
                IAction zoomFit = new ZoomFitAction(this, getGraphicalViewer());
                toolBarManager.add(zoomFit);
            }
            coolBarManager.add(toolBarManager);
            toolBarManager = new ToolBarManager(SWT.FLAT | SWT.WRAP | SWT.HORIZONTAL);
            pa = new PrintAction(ViewpointEditor.this, getGraphicalViewer());
            pa.setToolTipText(EditorMessages.viewpoint_editor_print_tooltip);
            toolBarManager.add(pa);
            coolBarManager.add(toolBarManager);
            coolBarManager.update(true);
        }
    }
