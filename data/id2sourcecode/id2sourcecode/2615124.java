    @Override
    public boolean logout() throws ClientProtocolException, IOException {
        final HttpPost login = new HttpPost("http://www.megaupload.com/");
        final MultipartEntity entity = new MultipartEntity();
        entity.addPart("logout", new StringBody("1"));
        login.setEntity(entity);
        final HttpResponse response = this.client.execute(login);
        response.getEntity().consumeContent();
        return true;
    }
