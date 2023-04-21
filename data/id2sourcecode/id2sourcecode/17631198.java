    public static Source.Blob blob(final URL url) {
        return new Source.Blob(urlToFilename(url)) {

            @Override
            protected InputStream toInputStream() throws IOException {
                URLConnection connection = url.openConnection();
                connection.setAllowUserInteraction(false);
                connection.connect();
                if (connection.getContentLength() == -1) {
                    try {
                        byte[] bytes = null;
                        bytes = readInputStream(connection.getInputStream(), bytes);
                        length = bytes.length;
                        return new ByteArrayInputStream(bytes);
                    } finally {
                        connection.getInputStream().close();
                    }
                } else {
                    length = connection.getContentLength();
                    return connection.getInputStream();
                }
            }

            @Override
            public String toString() {
                return super.toString() + "at URL '" + url + "'";
            }
        };
    }
