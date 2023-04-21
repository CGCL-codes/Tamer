    private void addStructure(@NonNls final String relativePath, final ZipOutputStream zos) throws IOException {
        ZipEntry e = new ZipEntry(relativePath + "/");
        e.setMethod(ZipEntry.STORED);
        e.setSize(0);
        e.setCrc(0);
        zos.putNextEntry(e);
        zos.closeEntry();
    }
