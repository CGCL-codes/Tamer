    public Boolean getBlank() {
        ChannelExt ext = getChannelExt();
        if (ext != null) {
            return ext.getBlank();
        } else {
            return null;
        }
    }
