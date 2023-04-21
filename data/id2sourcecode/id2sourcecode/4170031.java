    protected void readBlacklistFile() {
        NON_NAMES.clear();
        InputStream in = null;
        try {
            URL url = RsGbifOrg.authorityUrl(RsGbifOrg.FILENAME_BLACKLIST);
            log.debug("Reading " + url.toString());
            in = url.openStream();
            NON_NAMES.addAll(FileUtils.streamToSet(in));
        } catch (IOException e) {
            log.warn("Cannot read blacklist. Use empty set instead.", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        log.debug("loaded " + NON_NAMES.size() + " blacklisted names from rs.gbif.org");
    }
