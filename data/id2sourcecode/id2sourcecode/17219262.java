    protected void setupFileWriter() throws Exception {
        try {
            raFile = new RandomAccessFile(file, "rw");
            fileChannel = raFile.getChannel();
            fos = Channels.newOutputStream(fileChannel);
            writer = new OutputStreamWriter(fos);
        } catch (Exception e) {
            close();
            throw e;
        }
    }
