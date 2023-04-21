    @Override
    public void execute() throws Exception {
        if (url.isNull()) {
            throw new UnsetParameterException("url");
        }
        if (!authenticator.isNull()) {
            Authenticator.setDefault(getActualAuthenticator());
        }
        SyndFeedInput feedInput;
        URLConnection conn = new URL((String) url.getValue()).openConnection();
        conn.addRequestProperty("User-Agent", "Mozilla/4.76");
        feedInput = new SyndFeedInput();
        feed = feedInput.build(new XmlReader(conn));
    }
