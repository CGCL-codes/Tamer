    public static ServerResponseJson request(String request, Map<String, String> paramMap) {
        ServerResponseJson serverResponseJson = null;
        InputStream is = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            Builder uriBuilder = Uri.parse(String.format("http://%s:%s/%s", SERVER_HOST, SERVER_PORT, request)).buildUpon();
            if (paramMap != null) {
                for (Entry<String, String> entry : paramMap.entrySet()) {
                    uriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
                }
            }
            HttpResponse response = httpClient.execute(new HttpGet(uriBuilder.toString()));
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                is = entity.getContent();
                String jsonString = convertStreamToString(is);
                serverResponseJson = new ServerResponseJson(new JSONObject(jsonString));
            }
        } catch (Exception ex) {
            Log.e(ServerRequester.class.getName(), ex.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return serverResponseJson;
    }
