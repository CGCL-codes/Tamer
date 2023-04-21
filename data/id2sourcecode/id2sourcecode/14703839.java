    public Boolean getAccessByDir() {
        ChannelExt ext = getChannelExt();
        if (ext != null) {
            return ext.getAccessByDir();
        } else {
            return null;
        }
    }
