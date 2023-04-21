    private void compressFile(ZipOutputStream zipOutputStream, String basePath, String lang, File file) throws IOException {
        String entryName = getEntryName(basePath, file);
        setDescription(new I18nKey(ZIP_MESSAGE, DEFAULT_ZIP_MESSAGE), lang, file.getName(), destZip.getName());
        setInfo(new I18nKey(ZIP_INFO, DEFAULT_ZIP_INFO), lang);
        ZipEntry zipEntry = createEntry(entryName);
        zipEntry.setTime(file.lastModified());
        zipOutputStream.putNextEntry(zipEntry);
        FileInputStream is = new FileInputStream(file);
        copyToZipStream(is, zipOutputStream);
        zipOutputStream.flush();
        zipOutputStream.closeEntry();
    }
