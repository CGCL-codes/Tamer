    public DocumentLengthsReader(String filename) throws FileNotFoundException, IOException {
        file = new RandomAccessFile(new File(filename), "r");
        channel = file.getChannel();
        buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
    }
