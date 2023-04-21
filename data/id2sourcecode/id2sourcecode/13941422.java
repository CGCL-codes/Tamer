    public void clearOverlays() {
        if (line != null) {
            line.setVisible(false);
            mapPanel.getMap().removeOverlay(line);
            line = null;
        }
        Collection<Polyline> xSectionLines = mapPanel.getChannelManager().getXSectionLines();
        if (xSectionLines != null) {
            for (Polyline line : xSectionLines) {
                mapPanel.getMap().removeOverlay(line);
            }
        }
    }
