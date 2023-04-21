    public CZIPLoader(File file) throws IOException {
        ipFile = new RandomAccessFile(file, "r");
        Assert.assertResouceNotNull(ipFile, "unable to open QQWry.Dat file:" + file);
        ipBegin = readLong4(0);
        ipEnd = readLong4(4);
        if (ipBegin == -1 || ipEnd == -1) {
            ipFile.close();
            ipFile = null;
            throw new IOException("unknown format of QQWry.Dat file:" + file);
        }
        FileChannel fc = ipFile.getChannel();
        mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, ipFile.length());
        mbb.order(ByteOrder.LITTLE_ENDIAN);
    }
