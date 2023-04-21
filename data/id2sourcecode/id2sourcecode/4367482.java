    public void visitEntry(String name, ZipEntry e, ZipInputStream input, ZipOutputStream output) throws IOException {
        ZipEntry ze = new ZipEntry(e.getName());
        ze.setComment(e.getComment());
        ze.setTime(System.currentTimeMillis());
        output.putNextEntry(ze);
        StreamCopier sc = new StreamCopier(alternateData, output);
        sc.doCopy();
        output.closeEntry();
    }
