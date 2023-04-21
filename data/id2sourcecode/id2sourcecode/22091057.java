    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String resourcePath = getResourcePath(req);
        URL url = getClass().getResource(resourcePath);
        if (url == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        URLConnection conn = url.openConnection();
        resp.setContentType(conn.getContentType());
        int contentLength = conn.getContentLength();
        if (contentLength >= 0) resp.setContentLength(contentLength);
        InputStream in = conn.getInputStream();
        ServletOutputStream out = resp.getOutputStream();
        Tools.transfer(in, out);
        in.close();
        out.close();
    }
