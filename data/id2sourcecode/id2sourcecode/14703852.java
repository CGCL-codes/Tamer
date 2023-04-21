    public Integer getContentImgWidth() {
        ChannelExt ext = getChannelExt();
        if (ext != null) {
            return ext.getContentImgWidth();
        } else {
            return null;
        }
    }
