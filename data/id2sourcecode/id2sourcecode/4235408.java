    protected void showConfig(HttpServletRequest req, HttpServletResponse res) throws IOException {
        PrintWriter writer = res.getWriter();
        writer.println("<html>");
        writer.println("<body>");
        Enumeration servletContextManagers = Jasper.getServletContextManagers();
        while (servletContextManagers.hasMoreElements()) {
            HServletContextManager servletContextManager = (HServletContextManager) servletContextManagers.nextElement();
            HAbstractServletContextManagerConfig scmc = servletContextManager.getServletContextManagerConfig();
            writer.println("<table border>");
            writer.println("<tr><td bgcolor=\"#babf8c\" width=200>Max Threads</td><td bgcolor=white>" + scmc.getMaxThreads() + "</td></tr>");
            writer.println("<tr><td bgcolor=\"#babf8c\" width=200>Start Threads</td><td bgcolor=white>" + scmc.getStartThreads() + "</td></tr>");
            writer.println("<tr><td bgcolor=\"#babf8c\" width=200>Port</td><td bgcolor=white>" + scmc.getPort() + "</td></tr>");
            writer.println("<tr><td bgcolor=\"#babf8c\" width=200>Servlet Context Manager</td><td bgcolor=white>");
            showServletContextConfig(servletContextManager, req, res);
            writer.println("</td></tr>");
            writer.println("</table>");
        }
        writer.println("</body>");
        writer.println("</html>");
    }
