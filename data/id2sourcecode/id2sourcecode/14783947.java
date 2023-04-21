    public static String uncompressStream(InputStream is, String outputFile) throws IOException {
        GZIPInputStream gzis = new GZIPInputStream(is);
        FileOutputStream fos = new FileOutputStream(outputFile);
        byte[] buf = new byte[10000];
        int bytesRead;
        while ((bytesRead = gzis.read(buf)) > 0) fos.write(buf, 0, bytesRead);
        gzis.close();
        fos.close();
        return outputFile;
    }
