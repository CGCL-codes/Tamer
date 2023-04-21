    private void initialize() throws Exception {
        NULogger.getLogger().info("Getting startup cookies from localhostr.com");
        HttpGet httpget = new HttpGet("http://localhostr.com/");
        if (localhostrAccount.loginsuccessful) {
            httpget.setHeader("Cookie", LocalhostrAccount.getSessioncookie());
        }
        DefaultHttpClient httpclient;
        httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpResponse myresponse = httpclient.execute(httpget);
        HttpEntity myresEntity = myresponse.getEntity();
        localhostrurl = EntityUtils.toString(myresEntity);
        localhostrurl = CommonUploaderTasks.parseResponse(localhostrurl, "url : '", "'");
        NULogger.getLogger().log(Level.INFO, "Localhost url : {0}", localhostrurl);
        InputStream is = myresponse.getEntity().getContent();
        is.close();
    }
