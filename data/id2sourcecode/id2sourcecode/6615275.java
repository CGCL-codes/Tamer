    public MboxFile(String filepath, String mode) throws FileNotFoundException, IOException {
        this.file = new File(filepath);
        this.mode = mode;
        if (mode.equals(READ_WRITE)) lock = getChannel().lock();
    }
