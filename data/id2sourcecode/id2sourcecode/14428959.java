    @Override
    public ContentHeader load(ContentHeader inHeader) throws IOException {
        URLConnection connection = url.openConnection();
        initConnection(connection, inHeader);
        in = connection.getInputStream();
        encoding = connection.getContentEncoding();
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        ContentHeader outHeader = newContentHeader();
        ;
        if (connection instanceof HttpURLConnection) {
            String etag = connection.getHeaderField("ETag");
            long lastModified = connection.getHeaderFieldDate("Last-Modified", 0L);
            if (lastModified == 0l && inHeader != null) {
                lastModified = inHeader.getLastModified();
            }
            if (etag == null && inHeader != null) {
                etag = inHeader.getEtag();
            }
            outHeader.setEtag(etag);
            outHeader.setLastModified(lastModified);
        } else {
            try {
                File f = new File(getUrl().toURI());
                if (f.exists()) {
                    outHeader.setLastModified(f.lastModified());
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        LOGGER.debug("Loaded " + url);
        return outHeader;
    }
