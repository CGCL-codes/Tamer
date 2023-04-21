    protected CompositeMap loadByURL_NC(String url) throws IOException, SAXException {
        InputStream stream = null;
        try {
            URL the_url = new URL(url);
            stream = the_url.openStream();
            return parse(stream);
        } catch (Throwable thr) {
            throw new IOException(thr.getMessage());
        } finally {
            if (stream != null) stream.close();
        }
    }
