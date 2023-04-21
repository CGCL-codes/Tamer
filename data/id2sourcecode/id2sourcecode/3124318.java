    public JarOutputStream(File fout, Manifest man) throws IOException {
        super(fout);
        if (man == null) {
            throw new NullPointerException("man");
        }
        org.apache.tools.zip.ZipEntry e = new org.apache.tools.zip.ZipEntry(JarFile.MANIFEST_NAME);
        putNextEntry(e);
        man.write(new BufferedOutputStream(this));
        closeEntry();
    }
