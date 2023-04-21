    private void transferFromFile(FileChannel in, WritableByteChannel out) throws IOException {
        while (true) {
            long step = FileChannels.transferToByteChannel(in, out, this.blockSize);
            if (step > 0) bytesTransferred((int) step); else break;
        }
    }
