    private void markersMousePressed(MouseEvent e) {
        Debug.println(6, "mouse pressed: find marker");
        AClip c = getFocussedClip();
        ALayer l = c.getSelectedLayer();
        int chi = l.getPlotter().getInsideChannelIndex(e.getPoint());
        if (chi >= 0) {
            AChannel ch = l.getChannel(chi);
            markerChp = ch.getPlotter();
            markers = ch.getMarker();
            markerIndex = markers.searchNearestIndex((int) markerChp.graphToSampleX(e.getPoint().x), (int) Math.abs(markerChp.graphToSampleX(e.getPoint().x + markerSnapXDistance) - markerChp.graphToSampleX(e.getPoint().x)));
        }
    }
