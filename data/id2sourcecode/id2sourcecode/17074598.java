    @Override
    protected void applicationPreferencesChanged(PropertyChangeEvent event) {
        if (IPreferenceConstants.VIEWPOINTS_HIDE_PALETTE_ELEMENTS == event.getProperty()) {
            if (Boolean.TRUE == event.getNewValue()) {
                setPaletteViewpoint();
            } else {
                getPaletteRoot().setViewpoint(null);
            }
        } else if (IPreferenceConstants.VIEWPOINTS_HIDE_DIAGRAM_ELEMENTS == event.getProperty()) {
            getGraphicalViewer().setContents(getModel());
        } else {
            super.applicationPreferencesChanged(event);
        }
    }
