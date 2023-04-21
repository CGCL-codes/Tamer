    public String getTplChannel() {
        ChannelExt ext = getChannelExt();
        if (ext != null) {
            return ext.getTplChannel();
        } else {
            return null;
        }
    }
