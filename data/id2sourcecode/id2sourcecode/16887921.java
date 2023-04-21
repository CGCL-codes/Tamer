    @Test
    @Ignore
    public void basicHttpClientInputStreamUpload() throws IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        File f = new File("/Users/david/Desktop/meow_cat_art.jpg");
        String url = "http://www.pivotaltracker.com";
        String path = "/services/v3/projects/35420/stories/7587203/attachments";
        String token = "322c5f338282f3ee44804b4fcee3d07a";
        HttpPost httppost = new HttpPost(url + path);
        httppost.addHeader("X-TrackerToken", token);
        httppost.removeHeaders("Connection");
        InputStream is = null;
        try {
            MultipartEntity mpEntity = new MultipartEntity();
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
            SizedInputStreamBody cbFile = new SizedInputStreamBody(bis, f.getName(), f.length());
            System.out.println(cbFile.getTransferEncoding());
            mpEntity.addPart("Filedata", cbFile);
            httppost.setEntity(mpEntity);
            HttpResponse response = client.execute(httppost);
            String line;
            BufferedReader input = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) is.close();
            client.getConnectionManager().shutdown();
        }
    }
