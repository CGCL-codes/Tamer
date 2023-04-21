    private void intensitySelectionMousePressed(MouseEvent e) {
        Debug.println(6, "mouse pressed: find intensity point");
        AClip c = getFocussedClip();
        ALayer l = c.getSelectedLayer();
        int chi = l.getPlotter().getInsideChannelIndex(e.getPoint());
        if (chi >= 0) {
            AChannel ch = l.getChannel(chi);
            iChannelSelection = ch.getSelection();
            iChannelPlotter = ch.getPlotter();
            intensityPointIndex = iChannelSelection.searchNearestIntensityPointIndex(toNormalizedX(e.getPoint().x));
        }
    }
