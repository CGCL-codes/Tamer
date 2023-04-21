    private HttpURLConnection getConnection(String urlString, int maxTries, int waitSeconds) {
        HttpURLConnection connection = null;
        try {
            for (int attempt = 1; attempt <= maxTries; attempt++) {
                URL url = new URL(urlString);
                try {
                    connection = (HttpURLConnection) url.openConnection();
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        break;
                    } else {
                        connection = null;
                    }
                } catch (IOException ioe) {
                    if (attempt == maxTries) {
                        msgEntry = new MessageLogEntry(this, VERSION);
                        msgEntry.setAppContext("getConnection()");
                        msgEntry.setMessageText("IOException getting " + urlString);
                        msgEntry.setError(ioe.getMessage());
                        logger.logWarning(msgEntry);
                        break;
                    }
                }
                try {
                    Thread.sleep(waitSeconds * 1000);
                } catch (InterruptedException ie) {
                }
            }
        } catch (MalformedURLException mue) {
            msgEntry.setAppContext("getConnection()");
            msgEntry.setMessageText("Bad URL: " + urlString);
            msgEntry.setError(mue.getMessage());
            logger.logWarning(msgEntry);
        }
        return connection;
    }
