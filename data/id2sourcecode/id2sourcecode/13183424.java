    private String postData(String result, DefaultHttpClient httpclient, String id) {
        String responseMessage = null;
        try {
            HttpPost post = new HttpPost("http://3dforandroid.appspot.com/api/v2/" + id);
            StringEntity se = new StringEntity(result);
            se.setContentEncoding(HTTP.UTF_8);
            se.setContentType("application/json");
            post.setEntity(se);
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Accept", "*/*");
            HttpResponse response = httpclient.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream instream;
            instream = entity.getContent();
            responseMessage = read(instream);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseMessage;
    }
