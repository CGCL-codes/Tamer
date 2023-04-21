    public void mousePressed(MouseEvent e) {
        ALayer l = getFocussedClip().getSelectedLayer();
        int chi = l.getPlotter().getInsideChannelIndex(e.getPoint());
        if (chi >= 0) {
            graphicObjects = l.getChannel(chi).getGraphicObjects();
        }
        try {
            if (graphicObjects != null) {
                if (GGraphicObjects.getOperation() == GGraphicObjects.DRAW_TEXT) {
                    String t = JOptionPane.showInputDialog(GLanguage.translate("enterText"));
                    if (t == null) {
                        t = "";
                    }
                    GGraphicObjects.setCurrentText(t);
                }
                graphicObjects.mousePressed(e);
            }
            reloadGui();
            repaintFocussedClipEditor();
        } catch (Exception exc) {
        }
    }
