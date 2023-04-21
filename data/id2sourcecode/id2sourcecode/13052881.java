    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader in = request.getReader();
        PrintWriter out = response.getWriter();
        out.println("<!doctype html><head><title>File Upload GET</title></head><body>");
        out.println("<p>Content Length = '" + request.getContentLength() + "'</p>");
        out.println("<p>Content Type = '" + request.getContentType() + "'</p>");
        out.println("<p>");
        for (int i = 0; i < request.getContentLength(); i++) {
            out.write(in.read());
        }
        out.println("</p>");
        out.println("</body></html>");
    }
