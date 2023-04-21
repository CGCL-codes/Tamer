    private void writeZipEntries(ZipOutputStream zipOut, String directory, URLContent urlContent) throws IOException {
        ZipInputStream zipIn = null;
        try {
            zipIn = new ZipInputStream(urlContent.getJAREntryURL().openStream());
            for (ZipEntry entry; (entry = zipIn.getNextEntry()) != null; ) {
                String zipEntryName = entry.getName();
                Content siblingContent = new URLContent(new URL("jar:" + urlContent.getJAREntryURL() + "!/" + URLEncoder.encode(zipEntryName, "UTF-8").replace("+", "%20")));
                writeZipEntry(zipOut, directory + "/" + zipEntryName, siblingContent);
            }
        } finally {
            if (zipIn != null) {
                zipIn.close();
            }
        }
    }
