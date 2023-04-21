        public HttpResponse execute(HttpRequest request) {
            this.request = request;
            return new HttpResponse(LOCAL_SPEC_XML);
        }
