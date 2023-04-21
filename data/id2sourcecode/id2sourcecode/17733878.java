    public void transferToFully(FileChannel fileCh, long position, int count) throws IOException {
        while (count > 0) {
            waitForWritable();
            int nTransfered = (int) fileCh.transferTo(position, count, getChannel());
            if (nTransfered == 0) {
                if (position >= fileCh.size()) {
                    throw new EOFException("EOF Reached. file size is " + fileCh.size() + " and " + count + " more bytes left to be " + "transfered.");
                }
            } else if (nTransfered < 0) {
                throw new IOException("Unexpected return of " + nTransfered + " from transferTo()");
            } else {
                position += nTransfered;
                count -= nTransfered;
            }
        }
    }
