    private void zipAssembly(File tempDir, File assembly) throws IOException {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(assembly));
            List<File> files = DirLister.getListing(tempDir);
            for (File toZip : files) {
                ZipEntry ze = genZipEntry(tempDir, toZip);
                zos.putNextEntry(ze);
                this.writeEntryContent(toZip, zos);
                zos.closeEntry();
            }
        } finally {
            if (zos != null) {
                zos.close();
            }
        }
    }
