    public boolean writeToFile(File file) throws UnableToWriteWADFileException {
        try {
            return writeToFile(new RandomAccessFile(file, "rw").getChannel());
        } catch (FileNotFoundException e) {
            return false;
        }
    }
