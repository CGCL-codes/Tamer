    public FileBackedDeleteSet loadDeleteSet(boolean readOnly) throws IOException {
        File deleteSetFile = getDeleteSetFile();
        if (!deleteSetFile.exists()) return null;
        FileChannel deleteSetFileChannel = new RandomAccessFile(deleteSetFile, readOnly ? "r" : "rw").getChannel();
        return FileBackedDeleteSet.loadInstance(deleteSetFileChannel);
    }
