    private void writeZipFileLocales(ZipOutputStream zos, Locale[] locales, Locale[] referenceLocales) throws IOException {
        ZipEntry zipEntry = new ZipEntry(LOCALES_FILE_NAME);
        zos.putNextEntry(zipEntry);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zos));
        if (locales != null) {
            for (int i = 0; i < locales.length; i++) {
                writeLocaleEntry(locales[i], writer, false);
            }
        }
        if (referenceLocales != null) {
            for (int i = 0; i < referenceLocales.length; i++) {
                writeLocaleEntry(referenceLocales[i], writer, true);
            }
        }
        writer.flush();
        zos.closeEntry();
    }
