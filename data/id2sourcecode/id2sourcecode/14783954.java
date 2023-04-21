    public static String stream2file(InputStream is, String outputFile) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputFile);
        byte[] buf = new byte[10000];
        int bytesRead;
        while ((bytesRead = is.read(buf)) > 0) fos.write(buf, 0, bytesRead);
        is.close();
        fos.close();
        return outputFile;
    }
