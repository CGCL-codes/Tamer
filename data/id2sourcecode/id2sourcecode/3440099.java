    public void delete(Key key) {
        try {
            AppEngineEnv env = Kotan.get().getEnv();
            HttpHost target = new HttpHost(env.getHost(), env.getPort(), "http");
            HttpPost httpPost = new HttpPost("/_kotan/delete");
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("key", new InputStreamBody(new ByteArrayInputStream(toBinary(key)), "key"));
            httpPost.setEntity(reqEntity);
            HttpResponse response = clientManager.httpClient.execute(target, httpPost);
            System.out.println(response.getStatusLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
