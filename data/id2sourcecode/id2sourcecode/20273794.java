    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getRequestURI().substring(request.getServletPath().length());
        InputStream resource = ClassLoader.getSystemResourceAsStream("org/apache/zookeeper/graph/resources" + path);
        if (resource == null) {
            response.getWriter().println(path + " not found!");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        while (resource.available() > 0) {
            response.getWriter().write(resource.read());
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
