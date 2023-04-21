    public void createFile(String filename) throws IOException {
        zos.putNextEntry(new ZipEntry(filename));
    }
