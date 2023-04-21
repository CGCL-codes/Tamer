    public static void get(String url, String cap, String downloadDir) throws ClientProtocolException, IOException {
        url = url + (url.endsWith("/") ? "" : "/") + "download/" + cap;
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        if (response != null) {
            System.err.println(response.getStatusLine());
            HttpEntity ht = response.getEntity();
            final double filesize = ht.getContentLength();
            FileOutputStream out = new FileOutputStream(FileUtils.JoinPath(downloadDir, cap));
            OutputStreamProgress cout = new OutputStreamProgress(out, new ProgressListener() {

                @Override
                public void transfered(long bytes, float rate) {
                    int percent = (int) ((bytes / filesize) * 100);
                    String bar = ProgressUtils.progressBar("Download Progress: ", percent, rate);
                    System.out.print("\r" + bar);
                }
            });
            ht.writeTo(cout);
            out.close();
            System.out.println();
        } else {
            System.err.println("Error: response = null");
        }
        client.getConnectionManager().shutdown();
    }
