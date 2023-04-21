    public Integer getContentImgHeight() {
        ChannelExt ext = getChannelExt();
        if (ext != null) {
            return ext.getContentImgHeight();
        } else {
            return null;
        }
    }
