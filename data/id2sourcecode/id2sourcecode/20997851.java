    public OutputStream getEntryStream(String filename) throws IOException {
        os.putNextEntry(new ZipEntry(filename));
        return os;
    }
