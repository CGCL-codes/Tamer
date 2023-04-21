    public int route() {
        HashMap<Integer, SignalType> connections = new HashMap<Integer, SignalType>();
        int nSignals = fSignals.size();
        for (int i = 0; i < nSignals; i++) {
            VGSignal<NodeType, PortType, SignalType> signal = fSignals.get(i).fSignal;
            SignalType s = signal.getSignal();
            VGChannelAllocation ca = fSignals.get(signal);
            int n = ca.getNumPortConnections();
            for (int j = 0; j < n; j++) {
                VGPort<NodeType, PortType, SignalType> p = ca.getPortConnection(j);
                VGBox<NodeType, PortType, SignalType> box = p.getBox();
                VGSymbol<NodeType, PortType, SignalType> symbol = box.getSymbol();
                symbol.unTweakPortPosition(p.getPort());
                int pos = fLayout.getPortPosition(p).getY();
                SignalType s2 = connections.get(pos);
                if (s != null) {
                    if (s != s2) {
                        symbol.tweakPortPosition(p.getPort());
                        pos = fLayout.getPortPosition(p).getY();
                        connections.put(pos, s);
                    }
                } else {
                    connections.put(pos, s);
                }
            }
            VGChannel<NodeType, PortType, SignalType> lc = ca.getChannelConnLeft();
            if (lc != null) {
                VGChannelAllocation ca2 = lc.getSignalAllocation(signal);
                int pos = ca2.getChannelConnRightYPos();
                connections.put(pos, s);
            }
        }
        int penalty = 0;
        for (int i = 0; i < nSignals; i++) {
            VGSignal<NodeType, PortType, SignalType> signal = fSignals.get(i).fSignal;
            SignalType s = signal.getSignal();
            VGChannelAllocation ca = fSignals.get(signal);
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            int n = ca.getNumPortConnections();
            for (int j = 0; j < n; j++) {
                VGPort<NodeType, PortType, SignalType> p = ca.getPortConnection(j);
                int pos = fLayout.getPortPosition(p).getY();
                if (pos < min) min = pos;
                if (pos > max) max = pos;
            }
            VGChannel<NodeType, PortType, SignalType> lc = ca.getChannelConnLeft();
            if (lc != null) {
                VGChannelAllocation ca2 = lc.getSignalAllocation(signal);
                int pos = ca2.getChannelConnRightYPos();
                if (pos < min) min = pos;
                if (pos > max) max = pos;
            }
            VGChannel<NodeType, PortType, SignalType> rc = ca.getChannelConnRight();
            if (rc != null) {
                int pos = min;
                while (true) {
                    SignalType s2 = connections.get(pos);
                    if (s2 != null && s2 != s) {
                        pos += 5;
                        continue;
                    }
                    int nBoxes = getNumBoxes();
                    boolean coll = false;
                    for (int j = 0; j < nBoxes; j++) {
                        VGBox<NodeType, PortType, SignalType> box = getBox(j);
                        int boxY1 = box.getYPos();
                        int boxY2 = boxY1 + box.getHeight();
                        if (pos >= boxY1 && pos <= boxY2) {
                            coll = true;
                            pos = boxY2 + 5;
                            break;
                        }
                    }
                    if (!coll) {
                        break;
                    }
                }
                connections.put(pos, s);
                ca.setChannelConnRightYPos(pos);
                if (pos < min) min = pos;
                if (pos > max) max = pos;
            }
            penalty += (max - min);
        }
        return penalty;
    }
