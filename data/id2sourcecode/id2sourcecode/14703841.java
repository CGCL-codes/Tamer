    public Integer getPageSize() {
        ChannelExt ext = getChannelExt();
        if (ext != null) {
            return ext.getPageSize();
        } else {
            return null;
        }
    }
