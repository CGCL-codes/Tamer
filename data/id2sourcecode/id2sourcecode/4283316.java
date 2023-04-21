    public void test() {
        HttpResponse response = client.executeHttpGetRequest("http://www.google.com");
        super.assertNotNull(response);
        super.assertNotNull(response.getEntity());
    }
