    public void service(HttpServerRequest req, HttpServerResponse res) throws HttpServerException {
        String method = req.getMethod();
        final String responseEnc = "UTF-8";
        res.setContentType("text/html; charset=\"" + responseEnc + "\"");
        if ("GET".equalsIgnoreCase(method)) {
            Writer out = new PrintWriter(res.getOutputStream());
            try {
                out.write("<html><head><title>Delivery thread of WS-Messenger</title></head>");
                out.write("<body bgcolor='white'><h1>Delivery thread of WS-Messenger is running</h1>");
                out.write(RunTimeStatistics.getHtmlString());
                out.write("</body>");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("post".equalsIgnoreCase(method)) {
        }
    }
