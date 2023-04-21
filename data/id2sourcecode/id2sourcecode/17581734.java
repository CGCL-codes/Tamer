    public void loadData(String url, ResponseCallback responseCallBack) {
        HttpClient client = null;
        try {
            client = getHttpClient();
            HttpGet get = new HttpGet(url);
            HttpResponse response = client.execute(get);
            responseCallBack.callback(response);
        } catch (Throwable e) {
            LogUtil.error(e);
        } finally {
            if (client != null) client.getConnectionManager().shutdown();
        }
    }
