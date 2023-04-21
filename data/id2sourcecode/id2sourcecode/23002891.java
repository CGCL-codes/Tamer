    public String getVar(Channel c, int channel) {
        String retVal;
        String name = c.getName();
        if (isChannel(c)) {
            retVal = getChannelVar(name, channel);
        } else {
            retVal = getSubChannelVar(name, channel);
        }
        return retVal.replaceAll(" ", "");
    }
