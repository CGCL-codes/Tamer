    public static File writeTempFile(InputStream contentStream) throws IOException {
        File resourceFile = null;
        FileOutputStream resourceFileOutputStream = null;
        resourceFile = File.createTempFile("gateResource", ".tmp");
        resourceFileOutputStream = new FileOutputStream(resourceFile);
        resourceFile.deleteOnExit();
        if (contentStream == null) return resourceFile;
        int bytesRead = 0;
        final int readSize = 1024;
        byte[] bytes = new byte[readSize];
        while ((bytesRead = contentStream.read(bytes, 0, readSize)) != -1) resourceFileOutputStream.write(bytes, 0, bytesRead);
        resourceFileOutputStream.close();
        contentStream.close();
        return resourceFile;
    }
