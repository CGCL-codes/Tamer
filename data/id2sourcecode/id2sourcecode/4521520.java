    public void createFile(URL url, XFile targetFile) throws IOException {
        InputStream in = url.openStream();
        targetFile.createFrom(in);
        in.close();
    }
