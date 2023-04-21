    public void open() throws IOException {
        file = File.createTempFile("index", "idx");
        file.deleteOnExit();
        fos = new FileOutputStream(file);
        outputChannel = fos.getChannel();
    }
