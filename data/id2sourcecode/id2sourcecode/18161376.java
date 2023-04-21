    private void downloadOpenremoteZipFromBeehiveAndUnzip() {
        AndroidHttpClient httpClient = AndroidHttpClient.newInstance("OpenRemote");
        username = UserCache.getUsername(activity);
        password = UserCache.getPassword(activity);
        HttpGet httpGet = new HttpGet(beehiveRoot + username + "/openremote.zip");
        httpGet.setHeader("Authorization", "Basic " + encode(username, password));
        InputStream inputStream = null;
        try {
            HttpResponse response = httpClient.execute(httpGet);
            if (200 == response.getStatusLine().getStatusCode()) {
                Log.i("ServerlessConfigurator", httpGet.getURI() + " is available.");
                inputStream = response.getEntity().getContent();
                writeZipAndUnzip(inputStream);
            } else if (400 == response.getStatusLine().getStatusCode()) {
                Log.e("Serverless Configurator", "Not found", new Exception("400 Malformed"));
            } else if (401 == response.getStatusLine().getStatusCode() || 404 == response.getStatusLine().getStatusCode()) {
                Log.e("Serverless Configurator", "Not found", new Exception("401 Not authorized"));
                getCredentials();
            } else {
                Log.e("Serverless Configurator", "failed to download resources for template, The status code is: " + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            Log.e("SeverlessConfigurator", "failed to connect to Beehive.");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e("ServerlessConfigurator", "failed to close input stream while downloading " + httpGet.getURI());
                }
            }
            httpClient.close();
        }
        Log.i("ServerlessConfigurator", "Done downloading and unzipping files from OR.org");
    }
