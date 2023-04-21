    public <T> T invokeGZIP(Class<T> type, String urlSpec, Object... args) throws ERepublikException {
        try {
            URL url = new URL(this.getURL(urlSpec, args));
            LOG.info("Loading gziped data from " + url);
            InputStream in = new GZIPInputStream(url.openStream());
            return this.parse(type, new InputSource(in));
        } catch (MalformedURLException e) {
            throw new ERepublikException(e);
        } catch (IOException e) {
            throw new ERepublikException(e);
        }
    }
