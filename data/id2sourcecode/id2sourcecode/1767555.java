    @Override
    public boolean authenticate(Buddy user, String password) throws ConnectionException {
        if (user == null) throw new IllegalArgumentException("Null pointer in user");
        if (user.getName() == null) throw new IllegalArgumentException("Null pointer in user name");
        if (password == null) throw new IllegalArgumentException("Null pointer in password");
        this.user = user;
        ((MessagesParser) messagesHandler).setUser(user);
        URL url;
        try {
            url = new URL(getProperty("authentication.action"));
        } catch (NullPointerException ex) {
            throw new ConnectionException("No azet authentication action url was found.", ex);
        } catch (MalformedURLException ex) {
            throw new ConnectionException("Azet authentication action url is malformed", ex);
        }
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(getProperty("authentication.username"), user.getName());
        parameters.put(getProperty("authentication.password"), password);
        for (int i = 0; getProperty("authentication.hidden" + i + ".name") != null; i++) {
            parameters.put(getProperty("authentication.hidden" + i + ".name"), getProperty("authentication.hidden" + i + ".value"));
        }
        try {
            HttpURLHandler urlHandler = new HttpURLHandler(url, getProperty("authentication.method"), parameters, getProperty("encoding"));
            i9 = authenticationHandler.parse(urlHandler.getInputStream());
            authenticated = !i9.isEmpty();
            ((MessagesParser) messagesHandler).setMessageUrl(getProperty("messages.oneUrl"));
            ((MessagesParser) messagesHandler).setAdvancedReceiving(true);
        } catch (IOException ex) {
            throw new ConnectionException("Unable to read from/write to azet authenticaton action url", ex);
        }
        return authenticated;
    }
