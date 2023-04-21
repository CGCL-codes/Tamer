    public static BiopaxModel read(URL url, IChebiHelper chebiHelper) throws ReactionException, IOException {
        BiopaxModel model = null;
        InputStream in = null;
        try {
            in = url.openStream();
            model = read(in, chebiHelper);
        } catch (IOException e) {
            LOGGER.error("Unable to read from URL " + url, e);
        } finally {
            if (in != null) in.close();
        }
        return model;
    }
