    @Override
    public List<MessageBean> getMessages() throws ConnectionException {
        if (!authenticated) throw new IllegalStateException("Must be authenticated to receive messages.");
        URL url;
        try {
            url = new URL(getProperty("messages.allUrl"));
        } catch (NullPointerException ex) {
            throw new ConnectionException("No azet getMessages url was found.", ex);
        } catch (MalformedURLException ex) {
            throw new ConnectionException("Azet getMessages url is malformed", ex);
        }
        try {
            HttpURLHandler urlHandler = new HttpURLHandler(url);
            return new ArrayList<MessageBean>(splitterCondenser.condense(messagesHandler.parse(urlHandler.getInputStream())));
        } catch (IOException ex) {
            throw new ConnectionException("Unable to read from/write to azet getMessages url", ex);
        }
    }
