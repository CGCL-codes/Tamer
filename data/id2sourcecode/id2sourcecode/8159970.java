    private void openFileStream() {
        if (mFileStream != null) {
            return;
        }
        try {
            mFileStream = new FileInputStream(mFile);
            mFileChannel = mFileStream.getChannel();
            mFileBuffer = mFileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, mFileChannel.size());
            mFileBuffer.order(ByteOrder.LITTLE_ENDIAN);
        } catch (IOException exc) {
            throw new RuntimeIOException(exc.getMessage(), exc);
        }
    }
