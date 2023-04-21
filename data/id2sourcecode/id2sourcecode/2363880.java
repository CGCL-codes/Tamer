    private List<String> getRelsEntryNames(URL url) throws IOException {
        List<String> relsEntryNames = new LinkedList<String>();
        ZipInputStream zipInputStream = new ZipInputStream(url.openStream());
        ZipEntry zipEntry;
        while (null != (zipEntry = zipInputStream.getNextEntry())) {
            String entryName = zipEntry.getName();
            if (entryName.endsWith(".rels")) {
                relsEntryNames.add(entryName);
            }
        }
        return relsEntryNames;
    }
