    @Before
    public void setUp() {
        impl = new AbstractHttpClientDecorator(null) {

            public HttpResponse execute(HttpHost host, HttpRequest req, HttpContext ctx) throws IOException, ClientProtocolException {
                throw new IllegalStateException("not implemented");
            }
        };
    }
