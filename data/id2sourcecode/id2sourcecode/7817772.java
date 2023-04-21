    public void run() {
        URL url;
        try {
            url = new URL(urlString.trim());
        } catch (final MalformedURLException e1) {
            error = "invalid url";
            return;
        }
        InputStream stream;
        try {
            stream = url.openStream();
            try {
                final ByteArrayOutputStream sink = new ByteArrayOutputStream();
                InstallMapDialog.copy(sink, stream);
                contents = sink.toByteArray();
            } finally {
                stream.close();
            }
        } catch (final Exception e) {
            error = e.getMessage();
        }
    }
