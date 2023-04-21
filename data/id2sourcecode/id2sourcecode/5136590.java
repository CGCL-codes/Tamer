    public FLVWriter(FileOutputStream fos, ITag lastTag) {
        this.fos = fos;
        this.lastTag = lastTag;
        if (lastTag != null) {
            offset = lastTag.getTimestamp();
        }
        channel = this.fos.getChannel();
        out = ByteBuffer.allocate(1024);
        out.setAutoExpand(true);
    }
