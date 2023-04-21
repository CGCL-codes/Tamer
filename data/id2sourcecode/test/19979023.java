    private String getFileData() throws IOException {
        super.openFile("r");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while (in.available() != 0) bos.write(in.readByte());
        super.closeFile();
        return bos.toString();
    }
