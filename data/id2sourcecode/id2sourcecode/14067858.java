    public static URLConnection getConnectionInstance(ParameterHandler handler, URL url) throws UndefinedParameterError, IOException {
        URLConnection connection = url.openConnection();
        String userAgent = UserAgent.DEFAULT_USER_AGENT;
        if (handler.isParameterSet(PARAMETER_RANDOM_USER_AGENT) && handler.getParameterAsBoolean(PARAMETER_RANDOM_USER_AGENT)) {
            userAgent = UserAgent.getRandomUserAgent();
        } else if (handler.isParameterSet(PARAMETER_USER_AGENT)) {
            userAgent = handler.getParameterAsString(PARAMETER_USER_AGENT);
        }
        connection.setRequestProperty("User-Agent", userAgent);
        connection.setConnectTimeout(handler.getParameterAsInt(PARAMETER_CONNECTION_TIMEOUT));
        connection.setReadTimeout(handler.getParameterAsInt(PARAMETER_READ_TIMEOUT));
        return connection;
    }
