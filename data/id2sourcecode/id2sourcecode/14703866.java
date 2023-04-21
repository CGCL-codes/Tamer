    public ChannelTxt getChannelTxt() {
        Set<ChannelTxt> set = getChannelTxtSet();
        if (set != null && set.size() > 0) {
            return set.iterator().next();
        } else {
            return null;
        }
    }
