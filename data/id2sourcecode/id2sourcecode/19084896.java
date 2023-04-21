    public boolean transferFromChannelToFile(ReadableByteChannel channel, String fileName) throws IOException {
        if (!_location.exists()) {
            _location.mkdirs();
        }
        long dataLen = ChannelUtil.readLong(channel);
        if (dataLen < 0) return false;
        File file = new File(_location, fileName);
        RandomAccessFile raf = null;
        FileChannel fc = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            fc = raf.getChannel();
            return (fc.transferFrom(channel, 0, dataLen) == dataLen);
        } finally {
            if (raf != null) raf.close();
        }
    }
