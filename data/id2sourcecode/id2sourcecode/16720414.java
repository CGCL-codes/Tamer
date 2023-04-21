    private void open() throws GroupFileException {
        try {
            this.file = new RandomAccessFile(this.filename, "rw");
        } catch (FileNotFoundException fe) {
            throw new GroupFileException(file + " not found", fe);
        }
        this.channel = this.file.getChannel();
        this.lock = null;
        return;
    }
