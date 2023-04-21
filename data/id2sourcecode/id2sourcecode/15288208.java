    protected void applyPreferences() {
        IPreferenceStore store = UMLPlugin.getDefault().getPreferenceStore();
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, new Boolean(store.getBoolean(UMLPlugin.PREF_SHOW_GRID)));
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, new Boolean(store.getBoolean(UMLPlugin.PREF_SHOW_GRID)));
        int gridSize = store.getInt(UMLPlugin.PREF_GRID_SIZE);
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension(gridSize, gridSize));
        getGraphicalViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, new Boolean(store.getBoolean(UMLPlugin.PREF_SNAP_GEOMETRY)));
    }
