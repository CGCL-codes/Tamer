    public void runReport(ResourceCollection c, List<String> resources, OutputStream out) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(out);
        zipOut.setLevel(9);
        ZipEntry e = new ZipEntry(MANIFEST_FILENAME);
        zipOut.putNextEntry(e);
        XmlCollectionListing.INSTANCE.runReport(c, resources, zipOut);
        zipOut.closeEntry();
        for (String resourceName : resources) {
            long lastMod = c.getLastModified(resourceName);
            if (lastMod < 1) continue;
            Long checksum = c.getChecksum(resourceName);
            if (checksum == null) continue;
            e = new ZipEntry(resourceName);
            e.setTime(lastMod);
            zipOut.putNextEntry(e);
            InputStream resourceContent = c.getInputStream(resourceName);
            try {
                FileUtils.copyFile(resourceContent, zipOut);
            } finally {
                resourceContent.close();
            }
            zipOut.closeEntry();
        }
        zipOut.finish();
    }
