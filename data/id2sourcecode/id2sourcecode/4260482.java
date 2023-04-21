    private void globalSelectionMouseDragged(MouseEvent e) {
        Debug.println(6, "mouse dragged: create selection");
        Point draggedPoint = e.getPoint();
        endChannelIndex = layer.getPlotter().getInsideChannelIndex(draggedPoint);
        if ((startChannelIndex >= 0) && (endChannelIndex >= 0)) {
            int x = (int) layer.getChannel(endChannelIndex).getPlotter().graphToSampleX(draggedPoint.x);
            if (x < 0) x = 0;
            if (snapEnable) {
                int sx = AOToolkit.getNearestZeroCrossIndex(layer.getChannel(endChannelIndex).getSamples(), x, xSnapRange);
                if (sx != -1) {
                    x = sx;
                }
            }
            switch(tuningMode) {
                case NO_TUNING:
                    endSample = x;
                    break;
                case START_TUNING:
                    startSample = x;
                    break;
                case END_TUNING:
                    endSample = x;
                    break;
            }
            if (startChannelIndex > endChannelIndex) {
                int s = startChannelIndex;
                startChannelIndex = endChannelIndex;
                endChannelIndex = s;
            }
            for (int i = startChannelIndex; i <= endChannelIndex; i++) {
                layer.getChannel(i).modifySelection(Math.min(startSample, endSample), Math.abs(endSample - startSample));
            }
            wideSelectionCopy(e, layer.getChannel(startChannelIndex).getSelection());
        }
        repaintFocussedClipEditor();
        switch(tuningMode) {
            case START_TUNING:
            case END_TUNING:
                setCursor(e, tuningSelectionCursor);
                break;
        }
    }
