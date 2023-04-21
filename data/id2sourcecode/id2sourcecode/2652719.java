    private void open() throws PasswdFileException {
        try {
            this.file = new RandomAccessFile(this.filename, "rw");
        } catch (FileNotFoundException fe) {
            throw new PasswdFileException(file + " not found", fe);
        }
        this.channel = this.file.getChannel();
        return;
    }
