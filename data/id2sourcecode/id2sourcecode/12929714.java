    public FileChannelReader(final File file) {
        absolutePath = file.getAbsolutePath();
        try {
            in = new FileInputStream(file).getChannel();
            fileSize = in.size();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        logger.info("opened file: {}", absolutePath);
    }
