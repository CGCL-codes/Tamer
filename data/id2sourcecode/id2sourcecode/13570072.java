    private InputStream getInputStream(RssFeedEntity feed2Update) throws Exception {
        log.entering(UpdateTask.class.getName(), "parseInputStream");
        final String spec = feed2Update.getUrl();
        final URL url = new URL(spec);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        final InputStream inputStream = connection.getInputStream();
        log.exiting(UpdateTask.class.getName(), "getInputStream");
        return inputStream;
    }
