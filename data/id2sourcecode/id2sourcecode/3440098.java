    public void upload(EntityModel model) {
        try {
            AppEngineEnv env = Kotan.get().getEnv();
            HttpHost target = new HttpHost(env.getHost(), env.getPort(), "http");
            HttpPost httpPost = new HttpPost("/_kotan/upload");
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("entity", new InputStreamBody(new ByteArrayInputStream(toBinary(model.getEntity())), "entity"));
            httpPost.setEntity(reqEntity);
            HttpResponse response = clientManager.httpClient.execute(target, httpPost);
            System.out.println(response.getStatusLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
