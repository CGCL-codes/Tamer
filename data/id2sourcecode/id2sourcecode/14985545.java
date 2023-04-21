    protected void loadProperties() {
        GraphicalViewer graphicalviewer = getGraphicalViewer();
        if (diagram != null) {
            graphicalviewer.setProperty(ToggleSnapToGridAction.SNAPTOGRID, new Boolean(diagram.isSnapOn()));
            graphicalviewer.setProperty("SnapToGrid.isVisible", new Boolean(diagram.isGridVisible()));
        }
        IPreferenceStore store = DBDesignerPlugin.getDefault().getPreferenceStore();
        ERDConstants.TITLE_COLOR = new Color(null, PreferenceConverter.getColor(store, ERDConstants.TITLE_COLOR_PREF));
        ERDConstants.DETAIL_COLOR = new Color(null, PreferenceConverter.getColor(store, ERDConstants.DETAIL_COLOR_PREF));
        ERDConstants.CONNECTION_COLOR = new Color(null, PreferenceConverter.getColor(store, ERDConstants.CONNECTION_COLOR_PREF));
        ERDConstants.GRID_COLOR = new Color(null, PreferenceConverter.getColor(store, ERDConstants.GRID_COLOR_PREF));
        ERDConstants.TITLE_FONT = new Font(null, PreferenceConverter.getFontData(store, ERDConstants.TITLE_FONT_PREF));
        ERDConstants.DETAIL_FONT = new Font(null, PreferenceConverter.getFontData(store, ERDConstants.DETAIL_FONT_PREF));
        ERDConstants.CONNECTION_FONT = new Font(null, PreferenceConverter.getFontData(store, ERDConstants.CONNECTION_FONT_PREF));
        ERDConstants.CONNECTION_WIDTH = store.getInt(ERDConstants.CONNECTION_WIDTH_PREF);
        int gridSpacing = store.getInt(ERDConstants.GRID_SPACING_PREF);
        getGraphicalViewer().setProperty("SnapToGrid.GridSpacing", new Dimension(gridSpacing, gridSpacing));
        ERColumnEditPart.updateFonts(ERDConstants.DETAIL_FONT);
    }
