    public Channel[] getChannels() {
        Vector v = new Vector(channels.size());
        Enumeration channelEnum = channels.elements();
        while (channelEnum.hasMoreElements()) {
            Channel c = (Channel) channelEnum.nextElement();
            v.addElement(c);
        }
        int numChannels = v.size();
        if (numChannels > 0) {
            Channel[] retArray = new Channel[numChannels];
            for (int x = 0; x < numChannels; x++) {
                retArray[x] = (Channel) v.elementAt(x);
            }
            return retArray;
        }
        return null;
    }
