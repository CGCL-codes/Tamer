    public String getLink() {
        ChannelExt ext = getChannelExt();
        if (ext != null) {
            return ext.getLink();
        } else {
            return null;
        }
    }
