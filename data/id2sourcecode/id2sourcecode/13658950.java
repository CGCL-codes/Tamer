    public static File createTempFile(URL url) {
        try {
            InputStream is = url.openStream();
            byte[] content = StreamUtil.readByteContent(is);
            File tmpFile = File.createTempFile("dft", null);
            saveToFile(content, tmpFile, true);
            return tmpFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
