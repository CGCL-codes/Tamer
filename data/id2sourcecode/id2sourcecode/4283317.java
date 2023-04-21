    public void testError() {
        try {
            HttpResponse response = client.executeHttpGetRequest("http://www.");
        } catch (Throwable e) {
            return;
        }
        super.fail();
    }
