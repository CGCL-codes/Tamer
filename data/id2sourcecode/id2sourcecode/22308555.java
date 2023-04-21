    public void create(long capacity) throws IOException {
        if (capacity < MIN_CAPACITY) {
            throw new IllegalArgumentException("Minimum store capacity is " + MIN_CAPACITY + " fragments");
        }
        final File file = new File(getPath());
        file.createNewFile();
        FileOutputStream fos = null;
        FileChannel fileChannel = null;
        try {
            fos = new FileOutputStream(file);
            fileChannel = fos.getChannel();
            fileChannel.position(capacity * FRAGMENT_SIZE - 1);
            fos.write(0);
            logger.log(Level.INFO, "Created a new " + capacity + "MB store at " + getPath());
        } finally {
            if (fileChannel != null) {
                try {
                    fileChannel.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, null, e);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, null, e);
                }
            }
        }
    }
