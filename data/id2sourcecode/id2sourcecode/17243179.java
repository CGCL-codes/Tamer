            public URLStreamHandler createURLStreamHandler(String protocol) {
                if (protocol.equalsIgnoreCase("itrdb")) return new URLStreamHandler() {

                    protected URLConnection openConnection(URL url) throws IOException {
                        return new ItrdbURLConnection(url);
                    }
                }; else return null;
            }
