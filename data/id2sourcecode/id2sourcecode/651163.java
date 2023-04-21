    public OutputWindow[] getUserPanels(String user) {
        Vector v = new Vector();
        for (Enumeration e = channel_windows.elements(); e.hasMoreElements(); ) {
            ChannelWindow cw = (ChannelWindow) e.nextElement();
            Channel c = cw.getChannel();
            if (c.contains(user)) {
                v.addElement(cw);
            }
        }
        OutputWindow pw = getPrivate(user);
        if (null != pw) {
            v.addElement(pw);
        }
        OutputWindow[] a = new OutputWindow[v.size()];
        v.copyInto(a);
        return a;
    }
