    public boolean writeToFile(String filename) throws UnableToWriteWADFileException {
        try {
            File temp = new File(wadfilelocation.getParentFile().getAbsoluteFile(), filename + ".wad");
            return writeToFile(new RandomAccessFile(new File(temp.getAbsolutePath()), "rw").getChannel());
        } catch (FileNotFoundException e) {
            return false;
        }
    }
