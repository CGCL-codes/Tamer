    public JarOutputStream(OutputStream out, Manifest man) throws IOException {
        super(out);
        if (man == null) {
            throw new NullPointerException("man");
        }
        ZipEntry e = new ZipEntry(JarFile.MANIFEST_NAME);
        putNextEntry(e);
        man.write(new BufferedOutputStream(this));
        closeEntry();
    }
