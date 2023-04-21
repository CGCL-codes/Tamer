    public static long uploadLog(File f) {
        long id = 0;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httppost = new HttpPost("http://packjacket.sourceforge.net/log/up.php");
            MultipartEntity mpEntity = new MultipartEntity();
            ContentBody cbFile = new FileBody(f, "image/jpeg");
            mpEntity.addPart("uploadedfile", cbFile);
            httppost.setEntity(mpEntity);
            RunnerClass.logger.info("executing request " + httppost.getRequestLine());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            RunnerClass.logger.info("Status: " + response.getStatusLine());
            if (resEntity != null) {
                String s = EntityUtils.toString(resEntity);
                RunnerClass.logger.info("Result: " + s);
                id = Long.parseLong(s.replaceFirst("222 ", "").replace("\n", ""));
            }
            if (resEntity != null) resEntity.consumeContent();
            httpclient.getConnectionManager().shutdown();
        } catch (Exception ex) {
            RunnerClass.logger.info("Internet not available");
        }
        return id;
    }
