    @Override
    protected void process(HttpServletRequest request, HttpServletResponse response, int method, Player player) {
        if (request.getParameter("theme") == null || !request.getParameter("theme").startsWith("http://")) {
            write(request, response, "Invalid request.");
            return;
        }
        try {
            URL url = new URL(request.getParameter("theme") + "/style.xml");
            URLConnection connection = url.openConnection();
            BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
            response.setHeader("Content-Type", "application/xml");
            if (connection.getHeaderField("Last-Modified") != null) response.setHeader("Last-Modified", connection.getHeaderField("Last-Modified"));
            if (connection.getHeaderField("Content-Encoding") != null) response.setHeader("Content-Encoding", connection.getHeaderField("Content-Encoding"));
            if (connection.getHeaderField("Content-Length") != null) response.setHeader("Content-Length", connection.getHeaderField("Content-Length"));
            if (connection.getHeaderField("Etag") != null) response.setHeader("Etag", connection.getHeaderField("Etag"));
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            byte[] bytes = new byte[2048];
            int length;
            while ((length = in.read(bytes)) != -1) {
                out.write(bytes, 0, length);
            }
            in.close();
            out.flush();
        } catch (Exception e) {
            write(request, response, "Invalid request.");
        }
    }
