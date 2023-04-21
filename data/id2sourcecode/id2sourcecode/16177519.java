    public String getAllChannelsAsJson(String blipItSvcHost) {
        HttpEntity httpEntity = null;
        String channelsJson = null;
        try {
            String blipItSvcUrl = String.format("http://%s/blipit/panic/channel", blipItSvcHost);
            HttpGet httpGet = new HttpGet(blipItSvcUrl);
            httpGet.addHeader("Content-Type", JSON_CONTENT_TYPE);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            httpEntity = httpResponse.getEntity();
            InputStream content = httpEntity.getContent();
            channelsJson = convertStreamToString(content);
        } catch (IOException e) {
            logError(e);
        } finally {
            closeEntity(httpEntity);
        }
        return channelsJson;
    }
