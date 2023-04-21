    private boolean referredFilesChanged(String fileUri) throws XLWrapException, MalformedURLException, IOException {
        Resource meta = getMetadataResource(fileUri);
        long created = getTimestamp(fileUri).getTimeInMillis();
        StmtIterator it = meta.listProperties(SOURCE_FILE_PROPERTY);
        if (!it.hasNext()) log.warn("No information on associated spreadsheet files found in meta data for <" + fileUri + ">!");
        String file;
        while (it.hasNext()) {
            file = it.nextStatement().getResource().getURI();
            if (FileUtils.isURI(file)) {
                URLConnection url = new URL(file).openConnection();
                if (url.getLastModified() > created) return true;
            } else if (FileUtils.isFile(file)) {
                File f = new File(file);
                if (f.lastModified() > created) return true;
            }
        }
        return false;
    }
