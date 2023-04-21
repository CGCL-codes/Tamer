    public static final void main(String[] args) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.addRequestInterceptor(new HttpRequestInterceptor() {

            public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
                if (!request.containsHeader("Accept-Encoding")) {
                    request.addHeader("Accept-Encoding", "gzip");
                }
            }
        });
        httpclient.addResponseInterceptor(new HttpResponseInterceptor() {

            public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
                HttpEntity entity = response.getEntity();
                Header ceheader = entity.getContentEncoding();
                if (ceheader != null) {
                    HeaderElement[] codecs = ceheader.getElements();
                    for (int i = 0; i < codecs.length; i++) {
                        if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                            response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                            return;
                        }
                    }
                }
            }
        });
        HttpGet httpget = new HttpGet("http://www.apache.org/");
        System.out.println("executing request " + httpget.getURI());
        HttpResponse response = httpclient.execute(httpget);
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        System.out.println(response.getLastHeader("Content-Encoding"));
        System.out.println(response.getLastHeader("Content-Length"));
        System.out.println("----------------------------------------");
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String content = EntityUtils.toString(entity);
            System.out.println(content);
            System.out.println("----------------------------------------");
            System.out.println("Uncompressed size: " + content.length());
        }
    }
