    private final synchronized void putNextEntry(String name) throws IOException, FileNotFoundException {
        this.putNextEntry(new ZipEntry(name));
    }
