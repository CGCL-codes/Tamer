    static HttpResponse execute(HttpRequest request) throws IOException {
        try {
            return request.execute();
        } catch (HttpResponseException e) {
            if (e.response.statusCode == 302) {
                GoogleUrl url = new GoogleUrl(e.response.headers.location);
                request.url = url;
                new SessionIntercepter(request.transport, url);
                e.response.ignore();
                return request.execute();
            } else {
                throw e;
            }
        }
    }
