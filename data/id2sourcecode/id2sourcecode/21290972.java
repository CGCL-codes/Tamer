    public void mousePressed(MouseEvent e) {
        mouseDown = true;
        channelIndex = getFocussedClip().getSelectedLayer().getPlotter().getInsideChannelIndex(e.getPoint());
        channel = getFocussedClip().getSelectedLayer().getChannel(channelIndex);
        pressedX = (int) channel.getPlotter().graphToSampleX(e.getPoint().x);
        pressedSelectionOffset = channel.getSelection().getOffset();
        ctrlActive = GToolkit.isCtrlKey(e);
    }
