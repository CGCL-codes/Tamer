    public static void showVerificationcCode(String url, String fileUrl) throws ClientProtocolException, IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();
        int temp = 0;
        File file = new File(fileUrl);
        FileOutputStream out = new FileOutputStream(file);
        while ((temp = in.read()) != -1) {
            out.write(temp);
        }
        in.close();
        out.close();
    }
