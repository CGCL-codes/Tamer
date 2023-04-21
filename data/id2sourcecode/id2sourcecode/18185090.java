    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        System.out.println("ThreadServlet_MD: " + request.getRequestURI());
        response.setStatus(400);
        response.getWriter().write("<threads>");
        response.getWriter().write("</threads>");
    }
