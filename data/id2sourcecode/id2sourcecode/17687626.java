    private void compress(ZipOutputStream zipOutputStream, File folder, String basePath, String lang) throws IOException {
        File[] files = folder.listFiles(getFileFilter());
        for (File file : files) {
            if (file.isFile()) {
                compressFile(zipOutputStream, basePath, lang, file);
            } else if (file.isDirectory()) {
                String entryName = getEntryName(basePath, file) + "/";
                ZipEntry zipEntry = createEntry(entryName);
                zipEntry.setTime(file.lastModified());
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.flush();
                zipOutputStream.closeEntry();
                compress(zipOutputStream, file, basePath, lang);
            }
        }
    }
