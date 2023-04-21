    public void handle(HttpExchange he) throws IOException {
        String path = he.getRequestURI().toASCIIString();
        int split = path.indexOf('?');
        if (split != -1) {
            path = path.substring(0, split);
        }
        Logger.getLogger("ajax-handler").log(Level.SEVERE, "Connected. " + path);
        OutputStream response = he.getResponseBody();
        try {
            if (path.endsWith(".js")) {
                he.getResponseHeaders().add("content-type", "text/javascript");
            }
            InputStream fr = this.getClass().getResourceAsStream("/public" + path);
            he.sendResponseHeaders(200, 0);
            while (fr.available() > 0) {
                byte[] buf = new byte[1024];
                response.write(buf, 0, fr.read(buf));
            }
            response.close();
        } catch (IOException e) {
            he.sendResponseHeaders(404, 0);
            response.close();
        }
    }
