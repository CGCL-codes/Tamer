    @Override
    public void handle(HttpExchange arg0) throws IOException {
        byte[] b;
        try {
            InputStream is = url.openStream();
            b = new byte[is.available()];
            is.read(b);
            is.close();
        } catch (Exception e) {
            arg0.sendResponseHeaders(500, 0);
            return;
        }
        arg0.sendResponseHeaders(200, b.length);
        OutputStream os = arg0.getResponseBody();
        os.write(b);
        os.close();
        arg0.close();
    }
