        @Override
        protected void configure() {
            bind(RequestPipeline.class).toInstance(new RequestPipeline() {

                public HttpResponse execute(HttpRequest request) {
                    return null;
                }

                public void normalizeProtocol(HttpRequest request) throws GadgetException {
                }
            });
            bind(GadgetSpecFactory.class).toInstance(new GadgetSpecFactory() {

                public GadgetSpec getGadgetSpec(GadgetContext context) {
                    return null;
                }
            });
        }
