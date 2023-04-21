    public long transferFromFileToChannel(String fileName, WritableByteChannel channel) throws IOException {
        long amount = 0;
        File file = new File(_location, fileName);
        RandomAccessFile raf = null;
        FileChannel fc = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            fc = raf.getChannel();
            long dataLen = fc.size();
            amount += ChannelUtil.writeLong(channel, dataLen);
            amount += fc.transferTo(0, dataLen, channel);
        } finally {
            if (raf != null) raf.close();
        }
        return amount;
    }
