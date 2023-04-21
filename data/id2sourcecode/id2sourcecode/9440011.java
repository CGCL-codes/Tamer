    public byte[] readFileIntoByteArray(String filename) throws RacoonFileAccessException {
        FileInputStream fstream;
        try {
            fstream = new FileInputStream(filename);
            FileChannel fChannel = fstream.getChannel();
            int size = (int) fChannel.size();
            return fChannel.map(FileChannel.MapMode.READ_ONLY, 0, size).array();
        } catch (FileNotFoundException e) {
            logger.error("readFileIntoByteArray(String)", e);
            throw new RacoonFileAccessException("File not found", filename);
        } catch (IOException e) {
            logger.error("readFileIntoByteArray(String)", e);
            throw new RacoonFileAccessException("File not found", filename);
        }
    }
