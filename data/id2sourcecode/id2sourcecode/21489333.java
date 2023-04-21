    protected URLConnection openConnection(URL url) throws IOException {
        return new URLConnection(url) {

            private String key;

            private String getKey() {
                if (key == null) {
                    String urlString = getURL().toExternalForm();
                    if (!urlString.startsWith(PROTOCOL + ":")) throw new OXFException("Presentation Server URL must start with oxf:");
                    key = urlString.substring(PROTOCOL.length() + 1, urlString.length());
                }
                return key;
            }

            public void connect() {
            }

            public InputStream getInputStream() {
                return ResourceManagerWrapper.instance().getContentAsStream(getKey());
            }

            public OutputStream getOutputStream() {
                return ResourceManagerWrapper.instance().getOutputStream(getKey());
            }

            public long getLastModified() {
                return ResourceManagerWrapper.instance().lastModified(getKey());
            }

            public int getContentLength() {
                return ResourceManagerWrapper.instance().length(getKey());
            }
        };
    }
