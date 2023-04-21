    public RootFileScannerInput(FileScanner scanner, String fileName) throws IOException {
        super(scanner, (new File(fileName)).getName());
        pushClosable(this.file = new RandomAccessFile(fileName, "r"));
        this.fileSize = this.file.getChannel().size();
        this.fileName = fileName;
    }
