    public Byte getAfterCheck() {
        ChannelExt ext = getChannelExt();
        if (ext != null) {
            return ext.getAfterCheck();
        } else {
            return null;
        }
    }
