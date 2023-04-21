    public void mouseMoved(MouseEvent e) {
        AClip c = ((GClipPanel) e.getSource()).getClip();
        int x = e.getPoint().x;
        final int D_MAX = 5;
        int dStart = Math.abs(c.getAudio().getPlotter().getXLoopStartPointer() - x);
        int dEnd = Math.abs(c.getAudio().getPlotter().getXLoopEndPointer() - x);
        selectedPointer = NO_POINTER;
        if (dEnd < D_MAX) {
            selectedPointer = LOOP_END_POINTER;
            ((Component) e.getSource()).setCursor(placeCursor);
        } else if (dStart < D_MAX) {
            selectedPointer = LOOP_START_POINTER;
            ((Component) e.getSource()).setCursor(placeCursor);
        } else {
            ((Component) e.getSource()).setCursor(defaultCursor);
        }
        if (GToolkit.isShiftKey(e)) {
            ((Component) e.getSource()).setCursor(scratchCursor);
            ALayer l = getFocussedClip().getSelectedLayer();
            int i = l.getPlotter().getInsideChannelIndex(e.getPoint());
            if (i >= 0) {
                AChannel ch = l.getChannel(i);
                AChannelPlotter cp = ch.getPlotter();
                int xx = (int) cp.graphToSampleX(e.getPoint().x);
                pluginHandler.getFocussedClip().getAudio().scratch(xx);
            }
        }
    }
