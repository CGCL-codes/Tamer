    public static String post(String url, HashMap<String, String> params, HashMap<String, File> files) throws HttpException {
        String ret = "";
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            for (String key : files.keySet()) {
                FileBody bin = new FileBody(files.get(key));
                reqEntity.addPart(key, bin);
            }
            for (String key : params.keySet()) {
                String val = params.get(key);
                reqEntity.addPart(key, new StringBody(val, Charset.forName("UTF-8")));
            }
            post.setEntity(reqEntity);
            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                ret = EntityUtils.toString(resEntity);
                Log.i("ARTags:HttpUtil:Post:Response", ret);
            }
        } catch (Exception e) {
            Log.e("ARTags:HttpUtil", "Error : " + e.getMessage());
            throw new HttpException(e.getMessage());
        }
        return ret;
    }
