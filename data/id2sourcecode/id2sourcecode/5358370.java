    private void transferToFile(ReadableByteChannel in, FileChannel out) throws IOException {
        while (true) {
            long step = FileChannels.transferFromByteChannel(in, out, this.blockSize);
            if (step > 0) bytesTransferred((int) step); else break;
        }
    }
