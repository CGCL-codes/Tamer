    public static void addFile(File zipFile, File addFile) throws Exception {
        OutputStream os = new BufferedOutputStream(new FileOutputStream(zipFile));
        ZipOutputStream zos = new ZipOutputStream(os);
        log.debug("Adding: " + addFile.getName());
        InputStream is = new BufferedInputStream(new FileInputStream(addFile), BUFFER);
        ZipEntry entry = new ZipEntry(addFile.getName());
        zos.putNextEntry(entry);
        copy(is, zos);
        log.debug("Added " + addFile.getName() + " to " + zipFile.getName());
    }
