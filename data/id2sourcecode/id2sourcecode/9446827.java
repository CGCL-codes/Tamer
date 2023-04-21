    public static void uploadFile(String url, String fileName) throws Exception {
        DefaultHttpClient client = new DefaultHttpClient();
        client.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY), new UsernamePasswordCredentials("test", "test2"));
        HttpPost httpPost = new HttpPost(url);
        File file = new File(fileName);
        System.out.println("File1 Length = " + file.length());
        FileBody bin = new FileBody(new File(fileName));
        StringBody comment = new StringBody("A Pointrel transaction file");
        MultipartEntity multipartEntityForPost = new MultipartEntity();
        multipartEntityForPost.addPart("uploaded", bin);
        multipartEntityForPost.addPart("comment", comment);
        httpPost.setEntity(multipartEntityForPost);
        HttpResponse response = client.execute(httpPost);
        HttpEntity entity = response.getEntity();
        System.out.println("statusLine>>>" + response.getStatusLine());
        if (entity != null) {
            entity.writeTo(System.out);
            entity.consumeContent();
        }
    }
