                    @Override
                    protected java.net.URLConnection openConnection(java.net.URL url) throws IOException {
                        return new java.net.URLConnection(url) {

                            @Override
                            public void connect() throws IOException {
                            }

                            @Override
                            public InputStream getInputStream() throws IOException {
                                InputStream result = null;
                                final Context context = Context.getCurrent();
                                if (context != null) {
                                    final Response response = context.getClientDispatcher().handle(new Request(Method.GET, this.url.toString()));
                                    if (response.getStatus().isSuccess()) {
                                        result = response.getEntity().getStream();
                                    }
                                }
                                return result;
                            }
                        };
                    }
