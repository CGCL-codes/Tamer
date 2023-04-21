    public String getNetRequestData(Context context, String requestJson) {
        InputStream is = null;
        HttpClient client = null;
        try {
            client = getHttpClient();
            HttpPost post = new HttpPost(NET_SERVER_URL);
            byte[] jsonByte = requestJson.getBytes("UTF-8");
            is = new ByteArrayInputStream(jsonByte);
            InputStreamEntity entity = new InputStreamEntity(is, jsonByte.length);
            post.setEntity(entity);
            post.addHeader("authorization", getUserData());
            post.addHeader("token", RequestJson.getPhoneToken());
            post.addHeader("version", "android2.0");
            post.addHeader("width", String.valueOf(BaseApp.getDisplayWidth()));
            post.addHeader("viewplace", "android");
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (final Throwable e) {
            LogUtil.warn(e.toString());
        } finally {
            CommonUtil.closeInputStream(is);
            if (client != null) client.getConnectionManager().shutdown();
        }
        return null;
    }
