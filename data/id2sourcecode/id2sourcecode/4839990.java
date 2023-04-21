    public static void main(String[] args) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getCredentialsProvider().setCredentials(new AuthScope("localhost", 8080), new UsernamePasswordCredentials("tomcat", "admin"));
        HttpGet httpget = new HttpGet("http://localhost:8080/manager/undeploy?path=/mvx");
        System.out.println("executing request" + httpget.getRequestLine());
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        if (entity != null) {
            System.out.println("Response content length: " + entity.getContentLength());
            entity.writeTo(System.out);
            System.out.println("Chunked?: " + entity.isChunked());
        }
        if (entity != null) {
            entity.consumeContent();
        }
        httpget = new HttpGet("http://localhost:8080/manager/list");
        System.out.println("executing request" + httpget.getRequestLine());
        response = httpclient.execute(httpget);
        entity = response.getEntity();
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        if (entity != null) {
            System.out.println("Response content length: " + entity.getContentLength());
            entity.writeTo(System.out);
            System.out.println("Chunked?: " + entity.isChunked());
        }
        if (entity != null) {
            entity.consumeContent();
        }
        httpget = new HttpGet("http://localhost:8080/manager/deploy?path=/Binary&war=file:/tmp/Binary.war");
        System.out.println("executing request" + httpget.getRequestLine());
        response = httpclient.execute(httpget);
        entity = response.getEntity();
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        if (entity != null) {
            System.out.println("Response content length: " + entity.getContentLength());
            entity.writeTo(System.out);
            System.out.println("Chunked?: " + entity.isChunked());
        }
        if (entity != null) {
            entity.consumeContent();
        }
    }
