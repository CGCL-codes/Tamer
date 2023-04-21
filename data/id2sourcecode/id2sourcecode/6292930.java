    public void propertyChange(PropertyChangeEvent event) {
        String property = event.getProperty();
        if (property.equals(ERDConstants.TITLE_FONT_PREF)) {
            ERDConstants.TITLE_FONT = new Font(null, getFontData(event, property));
            editor.getDiagram().broadcastToChildren(property, ERDConstants.TITLE_FONT);
        } else if (property.equals(ERDConstants.DETAIL_FONT_PREF)) {
            ERColumnEditPart.fontsUpdated = false;
            ERDConstants.DETAIL_FONT = new Font(null, getFontData(event, property));
            editor.getDiagram().broadcastToChildren(property, ERDConstants.DETAIL_FONT);
        } else if (property.equals(ERDConstants.CONNECTION_FONT_PREF)) {
            ERDConstants.CONNECTION_FONT = new Font(null, getFontData(event, property));
            editor.getDiagram().broadcastToConnections(property, ERDConstants.CONNECTION_FONT);
        } else if (property.equals(ERDConstants.TITLE_COLOR_PREF)) {
            ERDConstants.TITLE_COLOR = new Color(null, getColor(event, property));
            editor.getDiagram().broadcastToChildren(property, ERDConstants.TITLE_COLOR);
        } else if (property.equals(ERDConstants.DETAIL_COLOR_PREF)) {
            ERDConstants.DETAIL_COLOR = new Color(null, getColor(event, property));
            editor.getDiagram().broadcastToChildren(property, ERDConstants.DETAIL_COLOR);
        } else if (property.equals(ERDConstants.CONNECTION_COLOR_PREF)) {
            ERDConstants.CONNECTION_COLOR = new Color(null, getColor(event, property));
            editor.getDiagram().broadcastToConnections(property, ERDConstants.CONNECTION_COLOR);
        } else if (property.equals(ERDConstants.CONNECTION_WIDTH_PREF)) {
            ERDConstants.CONNECTION_WIDTH = Integer.parseInt((String) event.getNewValue());
            editor.getDiagram().broadcastToConnections(property, new Integer(ERDConstants.CONNECTION_WIDTH));
        } else if (property.equals(ERDConstants.GRID_SPACING_PREF)) {
            Integer integer = (Integer) event.getNewValue();
            editor.getGraphicalViewer().setProperty("SnapToGrid.GridSpacing", new Dimension(integer.intValue(), integer.intValue()));
        } else if (property.equals(ERDConstants.GRID_COLOR_PREF)) {
            ERDConstants.GRID_COLOR = new Color(null, getColor(event, property));
            editor.updateGridColor();
        }
    }
