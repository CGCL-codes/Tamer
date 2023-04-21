    public String getData(DefaultHttpClient httpclient, String id) {
        String responseMessage = "Error";
        try {
            HttpGet get = new HttpGet("http://3dforandroid.appspot.com/api/v2/get" + id + "?dbName=" + SQLiteBackup.Kind.getSimpleName());
            get.setHeader("Content-Type", "application/json");
            get.setHeader("Accept", "*/*");
            HttpResponse response = httpclient.execute(get);
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            responseMessage = read(instream);
            if (instream != null) instream.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseMessage;
    }
