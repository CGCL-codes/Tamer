    public void closeChangeset(final int aChangeset, final String aUserName, final String aPassword) throws IOException {
        URL url = new URL(Settings.getInstance().get(APIBASEURSETTING, DEFAULTAPIBASEURL) + "/changeset/" + aChangeset + "/close");
        System.err.println("DEBUG: URL= " + url.toString());
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.setRequestProperty("Authorization", "Basic " + (new sun.misc.BASE64Encoder()).encode((aUserName + ":" + aPassword).getBytes()));
        httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpCon.connect();
        int responseCode = httpCon.getResponseCode();
        LOG.info("response-code to closing of changeset: " + responseCode);
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IllegalStateException("Http-Status-code is not 200 but " + httpCon.getResponseCode() + " \"" + httpCon.getResponseMessage() + "\" Error=" + httpCon.getHeaderField("Error"));
        }
    }
