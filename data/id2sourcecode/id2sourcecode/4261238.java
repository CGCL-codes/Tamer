    @Override
    public IcdType doHttpRequest(HttpRequestBase httpRequest, Parser<? extends IcdType> parser) throws IcdParseException, IcdException, IOException {
        HttpResponse resp = executeHttpRequest(httpRequest);
        int statusCode = resp.getStatusLine().getStatusCode();
        switch(statusCode) {
            case 200:
                String content = EntityUtils.toString(resp.getEntity());
                if (DEBUG) Log.d(TAG, "result: " + content);
                return JsonUtil.consume(parser, content);
            default:
                resp.getEntity().consumeContent();
                checkServerStatus();
                throw new IcdException("网络连接错误, 请检查你的网络状态 ");
        }
    }
