    public void onResponseHeader(Object userContext, HeaderParser responseHeader) {
        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(listFile, "rwd");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        writeChannel = raf.getChannel();
    }
