    private Map<String, String> readSynonymFile(String file) {
        Map<String, String> synonyms = new HashMap<String, String>();
        InputStream in = null;
        try {
            URL url = RsGbifOrg.synonymUrl(file);
            log.debug("Reading " + url.toString());
            in = url.openStream();
            synonyms = FileUtils.streamToMap(in, 0, 1, true);
        } catch (IOException e) {
            log.warn("Cannot read synonym map from " + file + ". Use empty map instead.", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        log.debug("loaded " + synonyms.size() + " synonyms from file " + file);
        return synonyms;
    }
