    private void captureFileChannel(String fileName, File file) throws FileNotFoundException {
        FileOutputStream outStream;
        if (fileName != null) {
            outStream = new FileOutputStream(fileName);
        } else {
            outStream = new FileOutputStream(file);
        }
        super.out = new BufferedWriter(new OutputStreamWriter(outStream));
        this.fileChannel = outStream.getChannel();
    }
