    private File receiveFile(String fileName, String extension) throws IOException {
        File outPutFile = File.createTempFile("temp", extension + ZipManager.getExtension(fileName));
        URL url;
        try {
            url = new URL(fileName);
        } catch (Exception ex) {
            log.info("The filename: " + fileName + " is not an URL. Trying to get from a File object");
            url = new File(fileName).toURI().toURL();
        }
        log.info("Output file: " + outPutFile.getAbsolutePath());
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(outPutFile));
        log.info("Retrieving File: " + fileName);
        URLConnection urlc = url.openConnection();
        BufferedInputStream is = new BufferedInputStream(urlc.getInputStream());
        ZipManager.copyInputStream(is, os);
        log.info("Finished Retriving File " + outPutFile.getAbsolutePath());
        outPutFile = ZipManager.decompressGZipFileIfNeccessary(outPutFile);
        return outPutFile;
    }
