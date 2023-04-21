    public URLConnection openConnection(URL url) throws IOException {
        return new URLConnection(url) {

            @Override
            public void connect() throws IOException {
            }

            @Override
            public InputStream getInputStream() throws IOException {
                connect();
                Long idx = Long.parseLong(getURL().getPath());
                Resource res = documents.get(idx);
                if (res == null) {
                    throw new FileNotFoundException(getURL().toString());
                }
                return res.open();
            }
        };
    }
