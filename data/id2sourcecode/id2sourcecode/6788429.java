    public String getContentImg() {
        ChannelExt ext = getChannelExt();
        if (ext != null) {
            return ext.getContentImg();
        } else {
            return null;
        }
    }
