    public void copyFile(String inputFilename, String outputFilename) throws UnsupportedEncodingException, IOException {
        log.info("Copying '" + inputFilename + "' to '" + outputFilename + "'");
        File inputFile = null;
        InputStream inputStream = null;
        File outputFile = null;
        OutputStream outputStream = null;
        try {
            inputFile = new File(inputFilename);
            inputStream = new FileInputStream(inputFile);
        } catch (FileNotFoundException ex) {
            URL url = ClassLoader.getSystemResource(inputFilename);
            inputStream = url.openStream();
        }
        outputFile = new File(outputFilename);
        outputStream = new FileOutputStream(outputFile);
        int iRead = 0;
        do {
            byte[] bytesRead = new byte[256];
            iRead = inputStream.read(bytesRead);
            if (iRead > 0) {
                outputStream.write(bytesRead, 0, iRead);
            }
        } while (iRead > 0);
        outputStream.close();
        inputStream.close();
    }
