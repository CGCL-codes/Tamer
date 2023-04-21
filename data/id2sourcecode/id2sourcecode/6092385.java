    private void addDirectory(File dir) throws IOException {
        if (!dir.isDirectory()) {
            throw new IOException(dir.getName() + " is not a directory.");
        }
        String zipName = getZipName(dir);
        ZipEntry ze = new ZipEntry(zipName);
        ze.setTime(dir.lastModified());
        ze.setSize(0);
        ze.setCompressedSize(0);
        ze.setCrc(0);
        mZos.setMethod(ZipOutputStream.STORED);
        mZos.putNextEntry(ze);
        File[] contents = dir.listFiles();
        if (contents != null) {
            for (int i = 0; i < contents.length; i++) {
                if (contents[i].isDirectory()) {
                    addDirectory(contents[i]);
                } else {
                    addFile(contents[i]);
                }
            }
        }
    }
