    public Boolean getStaticContent() {
        ChannelExt ext = getChannelExt();
        if (ext != null) {
            return ext.getStaticContent();
        } else {
            return null;
        }
    }
