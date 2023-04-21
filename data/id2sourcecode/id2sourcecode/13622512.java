    public void _jspService(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
        PageContext pageContext = null;
        HttpSession session = null;
        ServletContext application = null;
        ServletConfig config = null;
        JspWriter out = null;
        Object page = this;
        JspWriter _jspx_out = null;
        PageContext _jspx_page_context = null;
        try {
            response.setContentType("text/html");
            pageContext = _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);
            _jspx_page_context = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _jspx_out = out;
            out.write("<html>\n");
            out.write("\n");
            out.write("<head>\n");
            out.write("</head>\n");
            out.write("\n");
            out.write("<body>\n");
            out.write("\n");
            out.write("<p>\n");
            out.write("<applet \n");
            out.write("   width=\"100%\" \n");
            out.write("   height=\"100%\" \n");
            out.write("   code=\"org.jboss.console.navtree.AppletBrowser\"\n");
            out.write("   archive=\"applet.jar\"\n");
            out.write("   >\n");
            out.write("   <!-- An empty refresh value disables the background refresh thread -->\n");
            out.write("   <param name=\"RefreshTime\" value=\"\">\n");
            out.write("   <param name=\"SessionId\" value=\"");
            out.print(request.getSession().getId());
            out.write("\">\n");
            out.write("   <param name=\"PMJMXName\" value=\"jboss.admin:service=PluginManager\">\n");
            out.write("</applet>\n");
            out.write("</p>\n");
            out.write("\n");
            out.write("</body>\n");
            out.write("\n");
            out.write("</html>\n");
        } catch (Throwable t) {
            if (!(t instanceof SkipPageException)) {
                out = _jspx_out;
                if (out != null && out.getBufferSize() != 0) try {
                    out.clearBuffer();
                } catch (java.io.IOException e) {
                }
                if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
            }
        } finally {
            _jspxFactory.releasePageContext(_jspx_page_context);
        }
    }
