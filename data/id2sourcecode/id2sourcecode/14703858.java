    public String getDescription() {
        ChannelExt ext = getChannelExt();
        if (ext != null) {
            return ext.getDescription();
        } else {
            return null;
        }
    }
