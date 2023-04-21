    public Boolean getListChild() {
        ChannelExt ext = getChannelExt();
        if (ext != null) {
            return ext.getListChild();
        } else {
            return null;
        }
    }
