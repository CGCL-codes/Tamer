    public String getChannelRule() {
        ChannelExt ext = getChannelExt();
        if (ext != null) {
            return ext.getChannelRule();
        } else {
            return null;
        }
    }
