    private void wideMarkerCopy(MouseEvent e, AChannelMarker orig) {
        if (GToolkit.isCtrlKey(e)) {
            ALayer l = getFocussedClip().getSelectedLayer();
            for (int i = 0; i < l.getNumberOfChannels(); i++) {
                AChannel ch = l.getChannel(i);
                if (ch.getMarker() != orig) {
                    ch.setMarker(new AChannelMarker(orig));
                }
            }
        }
    }
