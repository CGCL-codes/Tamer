    private void routeSignal(VGSignal<NodeType, PortType, SignalType> s, VGPort<NodeType, PortType, SignalType> source, VGPort<NodeType, PortType, SignalType> dest) {
        int sc = getCol(source);
        int dc = getCol(dest);
        if (source.isOutput()) sc++;
        if (dest.isOutput()) dc++;
        if (sc > dc) {
            int tmp;
            tmp = sc;
            sc = dc;
            dc = tmp;
            VGPort<NodeType, PortType, SignalType> tmpPrt = source;
            source = dest;
            dest = tmpPrt;
        }
        VGChannel<NodeType, PortType, SignalType> c = getChannel(sc);
        c.addPortConnection(source, s);
        while (sc < dc) {
            sc++;
            VGChannel<NodeType, PortType, SignalType> c2 = getChannel(sc);
            c.addChannelConnRight(s, c2);
            c2.addChannelConnLeft(s, c);
            c = c2;
        }
        c.addPortConnection(dest, s);
    }
