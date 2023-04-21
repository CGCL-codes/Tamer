    public static String uncompressZipStream(InputStream is, String outputFile) throws IOException {
        ZipInputStream zis = new ZipInputStream(is);
        FileOutputStream fos = new FileOutputStream(outputFile);
        if (zis.getNextEntry() == null) throw new IOException("Not ZIP format");
        byte[] buf = new byte[10000];
        int bytesRead;
        while ((bytesRead = zis.read(buf)) > 0) fos.write(buf, 0, bytesRead);
        zis.closeEntry();
        zis.close();
        fos.close();
        return outputFile;
    }
