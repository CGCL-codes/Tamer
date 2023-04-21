    private FileChannel getFileChannel() throws IOException {
        if (this.tempFileChannel == null) {
            if (this.tempFileRandomAccessHandle == null) this.tempFileRandomAccessHandle = new RandomAccessFile(this.tempFileHandle, this.tempFileMode);
            this.tempFileChannel = this.tempFileRandomAccessHandle.getChannel();
        }
        ;
        return this.tempFileChannel;
    }
