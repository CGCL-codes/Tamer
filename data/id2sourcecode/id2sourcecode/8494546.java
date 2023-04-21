    public RandomAccessFileReader(String fileName, long startPosition) throws IOException {
        this.file = openFile(fileName);
        this.channel = file.getChannel();
        file.seek(startPosition);
        position = startPosition;
    }
