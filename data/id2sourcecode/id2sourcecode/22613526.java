    protected void generatePageHeader(HttpServletRequest request, PrintStream out) throws IOException {
        out.println("<head>");
        out.println("<title>" + htmlTitle + "</title>");
        out.println(getIconString(request));
        out.println("<script>");
        BufferedInputStream javascript = new BufferedInputStream(OpOpenServlet.class.getResourceAsStream("deployJava.js"));
        byte[] buffer = new byte[4096];
        int read = javascript.read(buffer);
        while (read > 0) {
            out.write(buffer, 0, read);
            read = javascript.read(buffer);
        }
        out.println("</script>");
        out.println("</head>");
    }
