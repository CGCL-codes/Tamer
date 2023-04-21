    private void saveJFreeResources(File jFreeFile, Map jFreeRes) throws IOException {
        if (jFreeRes.isEmpty()) return;
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(jFreeFile));
        for (Iterator i = jFreeRes.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry) i.next();
            String filename = (String) e.getKey();
            Properties p = (Properties) e.getValue();
            zipOut.putNextEntry(new ZipEntry(filename));
            p.store(zipOut, PROP_FILE_HEADER);
            zipOut.closeEntry();
        }
        zipOut.close();
    }
