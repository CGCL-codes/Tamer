    private void globalSelectionMousePressed(MouseEvent e) {
        Debug.println(6, "mouse pressed: start select functionality");
        pressedPoint = e.getPoint();
        clip = getFocussedClip();
        layer = clip.getSelectedLayer();
        startChannelIndex = layer.getPlotter().getInsideChannelIndex(pressedPoint);
        if (startChannelIndex >= 0) {
            updateTuningMode(e.getPoint());
            AChannel c = layer.getChannel(startChannelIndex);
            AChannelSelection cs = c.getSelection();
            AChannelPlotter cp = c.getPlotter();
            int x = (int) cp.graphToSampleX(pressedPoint.x);
            if (x < 0) x = 0;
            if (snapEnable) {
                xSnapRange = (int) (cp.graphToSampleX(pressedPoint.x + 20) - x);
                int sx = AOToolkit.getNearestZeroCrossIndex(layer.getChannel(endChannelIndex).getSamples(), x, xSnapRange);
                if (sx != -1) {
                    x = sx;
                }
            }
            switch(tuningMode) {
                case NO_TUNING:
                    startSample = x;
                    break;
                case START_TUNING:
                    endSample = cs.getOffset() + cs.getLength();
                    break;
                case END_TUNING:
                    startSample = cs.getOffset();
                    break;
            }
            Debug.println(6, "tuning mode = " + tuningMode);
        }
    }
