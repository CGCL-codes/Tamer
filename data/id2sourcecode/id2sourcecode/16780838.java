    public void getFileChannel(FileChannel chan, long len) throws IOException {
        chan.transferFrom(Chan, chan.position(), len);
    }
