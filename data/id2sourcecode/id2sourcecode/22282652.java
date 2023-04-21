    public PxlFile(String fileName, boolean read, boolean write) {
        this.fileName = fileName;
        try {
            reader = new BufferedReader(new FileReader(fileName));
        } catch (IOException e) {
            throw new IoError("Cannot open file: " + e.getMessage());
        }
    }
