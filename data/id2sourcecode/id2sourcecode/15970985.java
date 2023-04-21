    public java.nio.channels.WritableByteChannel getChannelWritable() {
        alto.lang.buffer.Abstract iob = this.getCreateBuffer();
        return iob.getChannelWritable();
    }
