    public final void start(int readBufferSize, int objectBufferSize) throws IOException {
        this.mInputStream = new FileInputStream(this.mTempFileName);
        this.mFileChannel = this.mInputStream.getChannel();
        this.mByteInputStream = java.nio.channels.Channels.newInputStream(this.mFileChannel);
        this.mBufferedInputStream = new BufferedInputStream(this.mByteInputStream, readBufferSize);
        this.mObjectInputStream = new ObjectInputStream(this.mBufferedInputStream);
    }
