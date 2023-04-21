    @Override
    protected void writeExistings(HashSet<String> obsolete, ZipOutputStream zipOut) throws IOException {
        Enumeration entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zEntry = (ZipEntry) entries.nextElement();
            if (!obsolete.contains(zEntry.getName())) {
                ZipEntry newEntry = new ZipEntry(zEntry);
                InputStream is = zipFile.getInputStream(zEntry);
                try {
                    zipOut.putNextEntry(newEntry);
                    Util.copy(is, zipOut);
                    zipOut.closeEntry();
                } finally {
                    is.close();
                }
            }
        }
    }
