    protected HttpResponse execute(final HttpMethod meth, String postURL) throws IOException {
        final String url = (meth == HttpMethod.POST) ? postURL : this.toString();
        if (log.isLoggable(Level.FINER)) log.finer(meth + "ing: " + url);
        return RequestExecutor.instance().execute(this.retries, new RequestSetup() {

            public void setup(RequestDefinition req) throws IOException {
                req.init(meth, url);
                for (Map.Entry<String, String> header : headers.entrySet()) req.setHeader(header.getKey(), header.getValue());
                if (timeout > 0) req.setTimeout(timeout);
                if (meth == HttpMethod.POST && !params.isEmpty()) {
                    if (!hasBinaryAttachments) {
                        String queryString = createQueryString();
                        if (log.isLoggable(Level.FINER)) log.finer("POST data is: " + queryString);
                        req.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                        req.setContent(queryString.getBytes("utf-8"));
                    } else {
                        log.finer("POST contains binary data, sending multipart/form-data");
                        MultipartWriter writer = new MultipartWriter(req);
                        writer.write(params);
                    }
                }
            }
        });
    }
