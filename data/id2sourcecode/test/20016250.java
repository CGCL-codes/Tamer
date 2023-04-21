    private void storeLangInZip(String langID, Properties p, ZipOutputStream zipOut) throws IOException {
        Iterator i = prefixes.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry e = (Map.Entry) i.next();
            String prefix = (String) e.getKey();
            String filename = (String) e.getValue();
            Properties filt = filterProperties(p, prefix);
            if (filt.isEmpty()) continue;
            filename = dirName(filename) + purifyFileName(filename) + "_" + langID + RES_EXTENSION;
            if (filename.startsWith("/")) filename = filename.substring(1);
            zipOut.putNextEntry(new ZipEntry(filename));
            storeProperties(zipOut, filt);
            zipOut.closeEntry();
        }
    }
