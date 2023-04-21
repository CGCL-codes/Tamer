        public HttpResponse execute(HttpRequest request) {
            this.request = request;
            return new HttpResponse(BASIC_BUNDLE);
        }
