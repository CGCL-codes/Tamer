    public Store(int pWriteBufferSize) throws IOException {
        File fd = File.createTempFile("KETL.", ".spool");
        this.mTempFileName = fd.getAbsolutePath();
        this.mOutputStream = new FileOutputStream(fd);
        this.mFileChannel = this.mOutputStream.getChannel();
        this.mByteOutputStream = java.nio.channels.Channels.newOutputStream(this.mFileChannel);
        this.mBufferedOutputStream = new BufferedOutputStream(this.mByteOutputStream, pWriteBufferSize);
        this.mObjectOutputStream = new ObjectOutputStream(this.mBufferedOutputStream);
    }
