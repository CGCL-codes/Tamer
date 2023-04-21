    @Override
    public boolean login() throws ClientProtocolException, IOException {
        final HttpPost login = new HttpPost("http://www.megaupload.com/?c=login");
        final MultipartEntity entity = new MultipartEntity();
        login.setEntity(entity);
        entity.addPart("login", new StringBody("1"));
        entity.addPart("username", new StringBody(username));
        entity.addPart("password", new StringBody(password));
        final HttpResponse response = this.client.execute(login);
        final InputStream in = response.getEntity().getContent();
        final String body = IOUtil.getString(in);
        in.close();
        if (body.contains("Username and password do " + "not match. Please try again!")) {
            return false;
        }
        return true;
    }
