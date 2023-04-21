    public void doGet(HttpExchange exchange) throws FileNotFoundException, IOException {
        FileInputStream in = null;
        String file = exchange.getRequestURI().getPath().replaceAll("/converter", "").intern();
        if (file == "" || file == "/" || file == "/converter.html") {
            file = "/converter.html";
        } else {
            file = "/documentFormats.js";
        }
        file = "./web" + file;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        in = new FileInputStream(file);
        while (in.available() > 0) {
            out.write(in.read());
        }
        exchange.sendResponseHeaders(200, 0);
        exchange.getResponseBody().write(out.toByteArray());
        exchange.getResponseBody().close();
        exchange.close();
    }
