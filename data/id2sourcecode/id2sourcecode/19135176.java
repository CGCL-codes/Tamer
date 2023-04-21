                @Override
                protected URLConnection openConnection(URL url) throws IOException {
                    return new URLConnection(url) {

                        @Override
                        public void connect() throws IOException {
                        }

                        @Override
                        public InputStream getInputStream() throws IOException {
                            return new ByteArrayInputStream(htmlText.toString().getBytes("UTF-8"));
                        }
                    };
                }
