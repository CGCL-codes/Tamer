    protected void setUp() throws Exception {
        super.setUp();
        File[] tempFiles = new File[3];
        for (int i = 0; i < tempFiles.length; i++) {
            tempFiles[i] = File.createTempFile("testing", "tmp");
            tempFiles[i].deleteOnExit();
            FileWriter writer = new FileWriter(tempFiles[i]);
            writer.write(CONTENT);
            writer.close();
        }
        FileInputStream fileInputStream = new FileInputStream(tempFiles[0]);
        readOnlyChannel = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream(tempFiles[1]);
        writeOnlyChannel = fileOutputStream.getChannel();
        RandomAccessFile randomAccessFile = new RandomAccessFile(tempFiles[2], "rw");
        readWriteChannel = randomAccessFile.getChannel();
    }
