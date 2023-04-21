    public java.util.Enumeration getChannels() {
        Vector V = new Vector();
        for (int i = 0; i < channels.length; i++) V.addElement(channels[i][0]);
        return V.elements();
    }
