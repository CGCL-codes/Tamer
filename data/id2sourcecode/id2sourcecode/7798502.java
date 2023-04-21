    public static boolean gzipUncompressToFile(URL url, File outFilename) {
        InputStream input;
        try {
            input = url.openStream();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(outFilename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return gzipUncompressToFile(input, outputStream);
    }
