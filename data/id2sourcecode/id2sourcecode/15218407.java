    public void open() throws IOException {
        properties = new Properties();
        String basename = System.getProperty("user.home") + "/.eaton-product.";
        instanceCount = 0;
        while (fileLock == null && instanceCount < 20) {
            File file = new File(basename + instanceCount + ".properties");
            fileChannel = new RandomAccessFile(file, "rw").getChannel();
            try {
                fileLock = fileChannel.tryLock();
            } catch (OverlappingFileLockException e) {
            }
            if (fileLock == null) {
                fileChannel.close();
                instanceCount++;
            }
        }
        if (fileLock != null) {
            open = true;
            properties.load(Channels.newInputStream(fileChannel));
        }
    }
