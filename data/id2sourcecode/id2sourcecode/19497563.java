    public static final void main(String[] args) throws Exception {
        final HttpHost target = new HttpHost("issues.apache.org", 80, "http");
        setup();
        HttpClient client = createHttpClient();
        HttpRequest req = createRequest();
        System.out.println("executing request to " + target);
        HttpEntity entity = null;
        try {
            HttpResponse rsp = client.execute(target, req);
            entity = rsp.getEntity();
            System.out.println("----------------------------------------");
            System.out.println(rsp.getStatusLine());
            Header[] headers = rsp.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                System.out.println(headers[i]);
            }
            System.out.println("----------------------------------------");
            if (entity != null) {
                System.out.println(EntityUtils.toString(rsp.getEntity()));
            }
        } finally {
            if (entity != null) entity.consumeContent();
        }
    }
