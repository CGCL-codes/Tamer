    @Test
    public void doesNotExposeFailureIfAssessorSaysNotTo() throws Exception {
        resp = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_INTERNAL_SERVER_ERROR, "Bork");
        backend.setResponse(resp);
        impl = new FailureExposingHttpClient(backend, new ResponseFailureAssessor() {

            public boolean isFailure(HttpResponse response) {
                return false;
            }
        });
        HttpResponse result = impl.execute(host, req, ctx);
        assertSame(result, resp);
    }
