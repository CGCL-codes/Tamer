    private void storeRefLangInZip(ZipOutputStream zipOut) throws IOException {
        if (saveRefLang == null) return;
        Properties p = set.getProperties(saveRefLang);
        if (p.isEmpty()) return;
        zipOut.putNextEntry(new ZipEntry("ref.zip"));
        ZipOutputStream refZip = new ZipOutputStream(zipOut);
        storeLangInZip(saveRefLang, p, refZip);
        refZip.finish();
        zipOut.closeEntry();
    }
