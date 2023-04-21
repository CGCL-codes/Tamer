    private static Map<String, String> getOntDownloadMap(URL url) throws IOException {
        return getOntDownloadMap(new BufferedReader(new InputStreamReader(url.openStream())));
    }
