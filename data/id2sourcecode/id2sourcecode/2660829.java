    private MenuManager createContextMenu(GraphicalViewer viewer) {
        MenuManager menuManager = new MenuManager();
        menuManager.setRemoveAllWhenShown(true);
        menuManager.addMenuListener(new ActionMenuListener(viewer));
        return menuManager;
    }
