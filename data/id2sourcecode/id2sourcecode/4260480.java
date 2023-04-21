    private void updateTuningMode(Point p) {
        ALayer l = getFocussedClip().getSelectedLayer();
        int ci = l.getPlotter().getInsideChannelIndex(p);
        if (ci >= 0) {
            AChannel c = l.getChannel(ci);
            AChannelPlotter cp = c.getPlotter();
            AChannelSelection cs = c.getSelection();
            int xss = (int) cp.sampleToGraphX(cs.getOffset());
            int xse = (int) cp.sampleToGraphX(cs.getOffset() + cs.getLength());
            if (Math.abs(p.x - xss) < tuningXTolerance) {
                tuningMode = START_TUNING;
            } else if (Math.abs(p.x - xse) < tuningXTolerance) {
                tuningMode = END_TUNING;
            } else {
                tuningMode = NO_TUNING;
            }
        }
    }
