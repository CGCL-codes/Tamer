    public void uploadFile(String url, File file) throws ClientProtocolException, IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        client.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY), new UsernamePasswordCredentials(this.remoteUser, this.remotePassword));
        HttpPost httpPost = new HttpPost(url);
        log("File1 Length = " + file.length());
        FileBody bin = new FileBody(file);
        StringBody comment = new StringBody("A Pointrel transaction file");
        MultipartEntity multipartEntityForPost = new MultipartEntity();
        multipartEntityForPost.addPart("uploaded", bin);
        multipartEntityForPost.addPart("comment", comment);
        httpPost.setEntity(multipartEntityForPost);
        HttpResponse response = client.execute(httpPost);
        HttpEntity entity = response.getEntity();
        log("statusLine>>>" + response.getStatusLine());
        if (entity != null) {
            entity.consumeContent();
        }
    }
