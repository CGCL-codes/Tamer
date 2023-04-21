    @Test
    @Ignore
    public void basicHttpClientUpload() {
        DefaultHttpClient client = new DefaultHttpClient();
        File f = new File("/Users/david/Desktop/meow_cat_art.jpg");
        String url = "http://www.pivotaltracker.com";
        String path = "/services/v3/projects/35420/stories/7587203/attachments";
        String token = "322c5f338282f3ee44804b4fcee3d07a";
        HttpPost httppost = new HttpPost(url + path);
        httppost.addHeader("X-TrackerToken", token);
        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody cbFile = new FileBody(f, "image/jpeg");
        mpEntity.addPart("Filedata", cbFile);
        httppost.setEntity(mpEntity);
        try {
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
            client.getConnectionManager().shutdown();
        }
    }
