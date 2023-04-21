    private KeyHandler getKeyHandler() {
        GraphicalViewerKeyHandler keyHandler = new GraphicalViewerKeyHandler(editor.getGraphicalViewer());
        keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), parent.getActionRegistry().getAction(ActionFactory.DELETE.getId()));
        keyHandler.put(KeyStroke.getPressed((char) 3, 99, 262144), parent.getActionRegistry().getAction(CopyModelAction.ID));
        keyHandler.put(KeyStroke.getPressed((char) 22, 118, 262144), parent.getActionRegistry().getAction(PasteModelAction.ID));
        return keyHandler;
    }
