    public String getLogString() {
        StringBuilder b = new StringBuilder(stripTitle + "( ");
        Iterator<StripChannel> it = getChannelList().iterator();
        while (it.hasNext()) {
            final String channelName = it.next().channelID;
            b.append(channelName + ", ");
        }
        b.append(")");
        return b.toString();
    }
