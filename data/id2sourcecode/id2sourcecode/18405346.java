    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final String CONTENT_TYPE = "text/xml";
        resp.setContentType(CONTENT_TYPE);
        final URL url = new URL("http", req.getParameter(HOST), Integer.parseInt(req.getParameter(PORT)), "/exist/rest/db/" + req.getParameter(DB) + "?_query=" + req.getParameter(XQUERY));
        new StreamReader(url.openStream(), resp.getOutputStream()).read();
    }
