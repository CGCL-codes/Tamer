    private InputStream openZipEntry(URL url, String entryName) throws MalformedURLException, IOException {
        return makeZipEntryUrl(url, entryName).openStream();
    }
