    public static ZipContents extractZip(final File zipFile, final File tmpDir) throws IOException {
        final File tmpCopy = File.createTempFile(FileUtils.sanitizeFileName(zipFile.getName()), "copy");
        FileUtils.copyFile(zipFile, tmpCopy);
        final ZipFile zip = new ZipFile(tmpCopy, ZipFile.OPEN_READ);
        final Map<String, File> fileEntryMap = new LinkedHashMap<String, File>();
        final File tmpPathDir = new File(tmpDir.getAbsolutePath() + File.separator + FileUtils.sanitizeFileName(zipFile.getName()));
        tmpPathDir.mkdirs();
        final Enumeration<ZipEntry> enumerated = (Enumeration<ZipEntry>) zip.entries();
        while (enumerated.hasMoreElements()) {
            final ZipEntry zipEntry = enumerated.nextElement();
            final File out = FileUtils.copyFileFromZip(zip, zipEntry, tmpPathDir);
            fileEntryMap.put(out.getAbsolutePath(), out);
        }
        return new ZipContents(fileEntryMap, tmpPathDir);
    }
