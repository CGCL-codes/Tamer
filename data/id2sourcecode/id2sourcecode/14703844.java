    public Byte getFinalStep() {
        ChannelExt ext = getChannelExt();
        if (ext != null) {
            return ext.getFinalStep();
        } else {
            return null;
        }
    }
