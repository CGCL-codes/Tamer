    public void paint(VGSelectionProvider<NodeType, SignalType> aSelectionProvider) {
        VGGC gc = fLayout.getGC();
        int nBoxes = fBoxes.size();
        for (int i = 0; i < nBoxes; i++) {
            VGBox<NodeType, PortType, SignalType> box = fBoxes.get(i);
            box.paint(aSelectionProvider.isNodeSelected(box.getNode()));
        }
        int nSignals = fSignals.size();
        for (int i = 0; i < nSignals; i++) {
            VGSignal<NodeType, PortType, SignalType> signal = fSignals.get(i).fSignal;
            boolean selected = aSelectionProvider.isSignalSelected(signal.getSignal());
            VGChannelAllocation ca = fSignals.get(signal);
            int minConnY = Integer.MAX_VALUE;
            int maxConnY = Integer.MIN_VALUE;
            int n = ca.getNumPortConnections();
            for (int j = 0; j < n; j++) {
                VGPort<NodeType, PortType, SignalType> p = ca.getPortConnection(j);
                int pos = fLayout.getPortPosition(p).getY();
                if (pos < minConnY) minConnY = pos;
                if (pos > maxConnY) maxConnY = pos;
            }
            VGChannel<NodeType, PortType, SignalType> channel = ca.getChannelConnLeft();
            if (channel != null) {
                int pos = channel.getSignalAllocation(signal).getChannelConnRightYPos();
                if (pos < minConnY) minConnY = pos;
                if (pos > maxConnY) maxConnY = pos;
            }
            channel = ca.getChannelConnRight();
            if (channel != null) {
                int pos = ca.getChannelConnRightYPos();
                if (pos < minConnY) minConnY = pos;
                if (pos > maxConnY) maxConnY = pos;
            }
            int w = signal.getWidth();
            if (selected) {
                gc.setLineWidth(w * 2);
                gc.setForeground(VGColor.HIGHLIGHT);
            } else {
                gc.setLineWidth(w);
                gc.setForeground(VGColor.SIGNAL);
            }
            int sx, sy, dx, dy;
            sx = fXPos + ca.fSignalIdx * WIRE_WIDTH;
            dx = fXPos + ca.fSignalIdx * WIRE_WIDTH;
            sy = minConnY;
            dy = maxConnY;
            gc.drawLine(sx, sy, dx, dy);
            if (selected) {
                gc.setBackground(VGColor.HIGHLIGHT);
            } else {
                gc.setBackground(VGColor.SIGNAL);
            }
            if (channel != null) {
                sy = ca.getChannelConnRightYPos();
                dx = channel.getSignalXPos(signal);
                gc.drawLine(sx, sy, dx, sy);
                if (sy != maxConnY && sy != minConnY) {
                    gc.fillOval(sx, sy, WIRE_WIDTH / 3, WIRE_WIDTH / 3);
                }
            }
            for (int j = 0; j < n; j++) {
                VGPort<NodeType, PortType, SignalType> p = ca.getPortConnection(j);
                Position ppos = fLayout.getPortPosition(p);
                sy = ppos.getY();
                dx = ppos.getX();
                gc.drawLine(sx, sy, dx, sy);
                if (sy == maxConnY) continue;
                if (sy == minConnY) continue;
                gc.fillOval(sx, sy, WIRE_WIDTH / 3, WIRE_WIDTH / 3);
            }
        }
    }
