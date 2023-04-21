    public static void put(String filepath, String url) throws ClientProtocolException, IOException {
        url = url + (url.endsWith("/") ? "" : "/") + "upload?t=json";
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpPost post = new HttpPost(url);
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        File file = new File(filepath);
        final double filesize = file.length();
        ContentBody fbody = new FileBodyProgress(file, new ProgressListener() {

            @Override
            public void transfered(long bytes, float rate) {
                int percent = (int) ((bytes / filesize) * 100);
                String bar = ProgressUtils.progressBar("Uploading file to the gateway : ", percent, rate);
                System.err.print("\r" + bar);
                if (percent == 100) {
                    System.err.println();
                    System.err.println("wait the gateway is processing and saving your file");
                }
            }
        });
        entity.addPart("File", fbody);
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        if (response != null) {
            System.err.println(response.getStatusLine());
            HttpEntity ht = response.getEntity();
            String json = EntityUtils.toString(ht);
            JSONParser parser = new JSONParser();
            try {
                JSONObject obj = (JSONObject) parser.parse(json);
                System.out.println(obj.get("cap"));
            } catch (ParseException e) {
                System.err.println("Error during parsing the response: " + e.getMessage());
                System.exit(-1);
            }
        } else {
            System.err.println("Error: response = null");
        }
        client.getConnectionManager().shutdown();
    }
