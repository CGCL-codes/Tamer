    public Boolean getAllowUpdown() {
        ChannelExt ext = getChannelExt();
        if (ext != null) {
            return ext.getAllowUpdown();
        } else {
            return null;
        }
    }
