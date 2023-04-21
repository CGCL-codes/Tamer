        public HttpResponse execute(HttpRequest request) {
            requests.add(request);
            return response;
        }
