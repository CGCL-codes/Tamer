    private void configureKeyHandler() {
        KeyHandler keyHandler = new GraphicalViewerKeyHandler(viewer);
        keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), actionRegistry.getAction(DeleteAction.REMOVE));
        keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, SWT.CTRL), actionRegistry.getAction(DeleteAction.DELETE));
        viewer.setKeyHandler(keyHandler);
    }
