    public AccessImpl(File file, boolean read, boolean write) throws FileNotFoundException {
        this.r = read;
        this.w = write;
        String mode = "";
        if (this.r) {
            mode += "r";
        }
        if (this.w) {
            mode += "w";
        }
        try {
            this.delegate = new LRAFile(file, mode);
        } catch (IOException e) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
    }
