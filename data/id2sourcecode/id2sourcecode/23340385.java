    public static Response get(String httpUrl) {
        Response.StatusCode statusCode = Response.StatusCode.NORMAL;
        String content = null;
        DefaultHttpClient dhc = new DefaultHttpClient();
        HttpUriRequest request = new HttpGet(httpUrl);
        try {
            HttpResponse response = dhc.execute(request);
            response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                content = IOUtils.toString(instream, EntityUtils.getContentCharSet(entity));
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusCode = Response.StatusCode.EXCEPTION;
        }
        return newResponse(statusCode, content);
    }
