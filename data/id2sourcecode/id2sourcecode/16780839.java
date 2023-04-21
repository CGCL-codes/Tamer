    public void getLongFileChannel(FileChannel chan) throws IOException {
        long l = getLong();
        if (l > MAXLONGLEN) {
            throw new IOException("Long length exceeds max! " + l);
        }
        chan.transferFrom(Chan, chan.position(), l);
    }
