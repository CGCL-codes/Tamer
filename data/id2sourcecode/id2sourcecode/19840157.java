    public FileDisk(int i, String path, String prefix, long size) {
        super();
        id = i;
        try {
            fileName = path + File.separator + prefix + "-" + id + ".dat";
            logger.debug("File name is " + fileName);
            File file = new File(fileName);
            if (file.exists() == false) {
                file.createNewFile();
                logger.info("Create a new file");
            }
            if (file.length() != size) {
                RandomAccessFile f = new RandomAccessFile(file, "rw");
                f.setLength(size);
                f.close();
                logger.info("Set the file to size " + size);
            }
            RandomAccessFile aFile = new RandomAccessFile(fileName, "rwd");
            data = aFile.getChannel();
            logger.info("FileDisk for Chunk [" + i + "] inits");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
