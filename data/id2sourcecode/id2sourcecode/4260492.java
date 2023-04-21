    private void intensitySelectionMouseMoved(MouseEvent e) {
        AClip c = getFocussedClip();
        ALayer l = c.getSelectedLayer();
        int chi = l.getPlotter().getInsideChannelIndex(e.getPoint());
        if (chi >= 0) {
            AChannel ch = l.getChannel(chi);
            iChannelSelection = ch.getSelection();
            iChannelPlotter = ch.getPlotter();
            intensityPointIndex = iChannelSelection.searchNearestIntensityPointIndex(toNormalizedX(e.getPoint().x));
            float x = toNormalizedX(e.getPoint().x);
            iChannelSelection.setActiveIntensityPoint(x);
            repaintFocussedClipEditor();
        }
        if (GToolkit.isShiftKey(e)) {
            setCursor(e, intensityEraseCursor);
        } else {
            setCursor(e, intensityDrawCursor);
        }
    }
