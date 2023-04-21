    public boolean handleFeatureSelectionEvent(FeatureSelectionEvent evt) {
        if (!LinksMenu.igbLinksEnabled()) return false;
        if (apollo.config.Config.DEBUG) {
            if (!igbIsRunning()) {
                logger.debug("can't send off selection to IGB - it's not running");
                return false;
            }
            SeqFeatureI selectedFeat = evt.getFeature();
            if (selectedFeat == null) return false;
            try {
                URL url = makeRegionUrl(selectedFeat);
                logger.debug("Connecting to IGB with URL " + url);
                URLConnection conn = url.openConnection();
                conn.connect();
                conn.getInputStream().close();
            } catch (MalformedURLException me) {
                logger.debug("malformed url", me);
                return false;
            } catch (IOException ie) {
                logger.error("unable to connect to igb", ie);
                return false;
            }
        }
        return true;
    }
