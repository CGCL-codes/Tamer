    public GeotiffReader(String sourceFilename) throws IOException {
        this.sourceFilename = sourceFilename;
        this.sourceFile = new RandomAccessFile(sourceFilename, "r");
        this.theChannel = this.sourceFile.getChannel();
        this.tiffReader = new TIFFReader(this.theChannel);
        readTiffHeaders();
    }
