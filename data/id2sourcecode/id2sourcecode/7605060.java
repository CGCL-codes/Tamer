    public void visitEntry(String name, ZipEntry sourceEntry, ZipInputStream input, ZipOutputStream output) throws IOException {
        ZipEntry resultingEntry = new ZipEntry(sourceEntry.getName());
        resultingEntry.setComment(sourceEntry.getComment());
        resultingEntry.setTime(sourceEntry.getTime());
        output.putNextEntry(resultingEntry);
        StreamCopier sc = new StreamCopier(input, output);
        sc.doCopy();
        output.closeEntry();
    }
