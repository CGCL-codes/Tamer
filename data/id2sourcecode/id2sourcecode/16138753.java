    private void writeManifest() throws IOException {
        zipOut.putNextEntry(new ZipEntry(getZipEntryFilename(MANIFEST_FILENAME)));
        writeManifest(zipOut);
        zipOut.closeEntry();
    }
