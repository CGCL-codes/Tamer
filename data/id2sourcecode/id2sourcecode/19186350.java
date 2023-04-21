    public RandomAccessFileWriter(String fileName, boolean append) throws IOException {
        this.file = openFile(fileName);
        if (append) {
            this.file.seek(this.file.length());
        }
        this.channel = file.getChannel();
    }
