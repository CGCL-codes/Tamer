    public String doHttpRequest(HttpRequestBase httpRequest) throws IcdParseException, IcdException, IOException {
        if (DEBUG) Log.d(TAG, "doHttpRequest : " + httpRequest.getURI());
        HttpResponse resp = executeHttpRequest(httpRequest);
        int statusCode = resp.getStatusLine().getStatusCode();
        switch(statusCode) {
            case 200:
                String content = EntityUtils.toString(resp.getEntity());
                if (DEBUG) Log.d(TAG, "result: " + content);
                return content;
            case 500:
                resp.getEntity().consumeContent();
                throw new IcdException("IN成都服务器正在升级...");
            default:
                resp.getEntity().consumeContent();
                throw new IcdException("网络连接出错了哦... ");
        }
    }
