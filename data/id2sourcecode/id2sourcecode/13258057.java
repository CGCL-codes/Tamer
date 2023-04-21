    protected void openFile(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new IllegalArgumentException("Die Datei " + file + " existiert nicht.");
        }
        randomAccessFile = new RandomAccessFile(file, "rw");
        channel = randomAccessFile.getChannel();
    }
