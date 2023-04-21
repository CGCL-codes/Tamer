    public String getLogString() {
        StringBuilder b = new StringBuilder(name + "( ");
        Iterator<StripChannelBinding> it = getChannelBindings().iterator();
        while (it.hasNext()) {
            final String channelName = it.next().boundChannelName;
            b.append(channelName + ", ");
        }
        b.append(")");
        return b.toString();
    }
