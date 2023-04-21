    public Integer getTitleImgHeight() {
        ChannelExt ext = getChannelExt();
        if (ext != null) {
            return ext.getTitleImgHeight();
        } else {
            return null;
        }
    }
