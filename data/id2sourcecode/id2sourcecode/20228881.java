        @Override
        public HttpResponseMessage execute(final HttpMessage request, final Map<String, Object> parameters) throws IOException {
            request.headers.add(new OAuth.Parameter("User-Agent", userAgent));
            return super.execute(request, parameters);
        }
