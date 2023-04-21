    public void uploadChange(final int aChangeset, final String anOSCFile, final String aUserName, final String aPassword) throws IOException {
        URL url = new URL(Settings.getInstance().get(APIBASEURSETTING, DEFAULTAPIBASEURL) + "/changeset/" + aChangeset + "/upload");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("POST");
        httpCon.setRequestProperty("Authorization", "Basic " + (new sun.misc.BASE64Encoder()).encode((aUserName + ":" + aPassword).getBytes()));
        OutputStream out = httpCon.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(out, "UTF8");
        System.out.println("changeset we got uploading:\n" + anOSCFile);
        String modified = anOSCFile.replaceAll("changeset=\"[0-9]*\"", "changeset=\"" + aChangeset + "\"");
        System.out.println("changeset we are uploading:\n" + modified);
        writer.write(modified);
        writer.close();
        int responseCode = httpCon.getResponseCode();
        LOG.info("response-code to changeset: " + responseCode);
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IllegalStateException("Http-Status-code is not 200 but " + httpCon.getResponseCode() + " \"" + httpCon.getResponseMessage() + "\" Error=" + httpCon.getHeaderField("Error"));
        }
    }
