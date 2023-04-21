    public String postMethod(String host, String urlPostfix, List<NameValuePair> nvps) {
        logger.info("Facebook: @executing facebookPostMethod():" + host + urlPostfix);
        String responseStr = null;
        try {
            HttpPost httpost = new HttpPost(host + urlPostfix);
            httpost.addHeader("Accept-Encoding", "gzip");
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            HttpResponse postResponse = httpClient.execute(httpost);
            HttpEntity entity = postResponse.getEntity();
            logger.trace("Facebook: facebookPostMethod: " + postResponse.getStatusLine());
            if (entity != null) {
                InputStream in = entity.getContent();
                if (postResponse.getEntity().getContentEncoding().getValue().equals("gzip")) {
                    in = new GZIPInputStream(in);
                }
                StringBuffer sb = new StringBuffer();
                byte[] b = new byte[4096];
                int n;
                while ((n = in.read(b)) != -1) {
                    sb.append(new String(b, 0, n));
                }
                responseStr = sb.toString();
                in.close();
                logger.trace("Facebook: " + responseStr);
                entity.consumeContent();
            }
            logger.info("Facebook: Post Method done(" + postResponse.getStatusLine().getStatusCode() + "), response string length: " + (responseStr == null ? 0 : responseStr.length()));
        } catch (IOException e) {
            logger.warn("Facebook: ", e);
        }
        return responseStr;
    }
