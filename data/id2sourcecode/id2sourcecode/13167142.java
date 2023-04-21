        public HttpResponse execute(HttpRequest request) {
            if (request.getAuthType() == AuthType.NONE) {
                return plainFetcher.fetch(request);
            }
            return oauthFetcher.fetch(request);
        }
