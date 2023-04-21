    public Integer getCommentControl() {
        ChannelExt ext = getChannelExt();
        if (ext != null) {
            return ext.getCommentControl();
        } else {
            return null;
        }
    }
