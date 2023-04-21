    public HttpResponse execute(final HttpHost host, final HttpRequest req, final HttpContext ctx) throws IOException, ClientProtocolException {
        try {
            return wrapper.invoke(new Callable<HttpResponse>() {

                public HttpResponse call() throws Exception {
                    return backend.execute(host, req, ctx);
                }
            });
        } catch (IOException ioe) {
            throw (ioe);
        } catch (RuntimeException re) {
            throw (re);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
