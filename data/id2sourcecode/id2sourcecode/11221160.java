    public void affectStream(ZipOutputStream output) throws IOException {
        ZipEntry ze = new ZipEntry(name);
        ze.setTime(System.currentTimeMillis());
        output.putNextEntry(ze);
        StreamCopier sc = new StreamCopier(source, output);
        sc.doCopy();
        output.closeEntry();
    }
