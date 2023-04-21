    private static void addDirectory(ZipOutputStream zipOut, File toZip, boolean isRoot) throws IOException {
        if (toZip.getName().length() > 0 && !toZip.getName().equals("/") && !isRoot) {
            ZipEntry entry = new ZipEntry(toZip.getName() + "/");
            zipOut.putNextEntry(entry);
            zipOut.closeEntry();
        }
        for (File internalDir : toZip.listFiles()) {
            if (internalDir.isFile()) addFile(zipOut, internalDir); else addDirectory(zipOut, internalDir, false);
        }
    }
