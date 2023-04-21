    public void setEnabled(boolean enable) {
        if (enable == enabled) return;
        enabled = enable;
        if (previousEditDomain == null) previousEditDomain = getGraphicalViewer().getEditDomain();
        if (!enabled) {
            getGraphicalViewer().deselectAll();
            getGraphicalViewer().setEditDomain(lockedEditDomain);
        } else getGraphicalViewer().setEditDomain(previousEditDomain);
        actionRegistry.setEnabled(enable);
        updateActions();
    }
