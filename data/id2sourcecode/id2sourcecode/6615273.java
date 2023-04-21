    public MboxFile(File file, String mode) throws FileNotFoundException, IOException {
        this.file = file;
        this.mode = mode;
        if (mode.equals(READ_WRITE)) lock = getChannel().lock();
    }
