    public String getTplContent() {
        ChannelExt ext = getChannelExt();
        if (ext != null) {
            return ext.getTplContent();
        } else {
            return null;
        }
    }
