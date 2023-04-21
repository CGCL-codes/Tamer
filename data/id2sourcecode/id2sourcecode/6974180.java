    public synchronized void adjustmentValueChanged(AdjustmentEvent e) {
        if (!running2 || imp.isHyperStack()) {
            if (e.getSource() == cSelector) {
                c = cSelector.getValue();
                if (c == imp.getChannel() && e.getAdjustmentType() == AdjustmentEvent.TRACK) return;
            } else if (e.getSource() == zSelector) {
                z = zSelector.getValue();
                int slice = hyperStack ? imp.getSlice() : imp.getCurrentSlice();
                if (z == slice && e.getAdjustmentType() == AdjustmentEvent.TRACK) return;
            } else if (e.getSource() == tSelector) {
                t = tSelector.getValue();
                if (t == imp.getFrame() && e.getAdjustmentType() == AdjustmentEvent.TRACK) return;
            }
            updatePosition();
            notify();
        }
    }
