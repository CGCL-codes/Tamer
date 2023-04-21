    public String getTitle() {
        ChannelExt ext = getChannelExt();
        if (ext != null) {
            return ext.getTitle();
        } else {
            return null;
        }
    }
