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
            response.setContentType("text/html; charset=UTF-8");
            pageContext = _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);
            _jspx_page_context = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _jspx_out = out;
            out.write("\n");
            out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \r\n");
            out.write("                    \"http://www.w3.org/TR/html4/loose.dtd\">\r\n");
            out.write("<html>\r\n");
            out.write("<head>\r\n");
            out.write("  <script src=\"http://code.jquery.com/jquery-latest.js\"></script>\r\n");
            out.write("  <link rel=\"stylesheet\" href=\"http://dev.jquery.com/view/trunk/themes/flora/flora.all.css\" type=\"text/css\" media=\"screen\" title=\"Flora (Default)\">\r\n");
            out.write("  <script type=\"text/javascript\" src=\"http://dev.jquery.com/view/trunk/ui/ui.tabs.js\"></script>\r\n");
            out.write("  <script>\r\n");
            out.write("  $(document).ready(function(){\r\n");
            out.write("    $(\"#example > ul\").tabs();\r\n");
            out.write("  });\r\n");
            out.write("  </script>\r\n");
            out.write("  \r\n");
            out.write("</head>\r\n");
            out.write("<body>\r\n");
            out.write("  \r\n");
            out.write("        <div id=\"example\" class=\"flora\">\r\n");
            out.write("            <ul>\r\n");
            out.write("\r\n");
            out.write("                <li><a href=\"#fragment-1\"><span>One</span></a></li>\r\n");
            out.write("                <li><a href=\"#fragment-2\"><span>Two</span></a></li>\r\n");
            out.write("                <li><a href=\"#fragment-3\"><span>Three</span></a></li>\r\n");
            out.write("            </ul>\r\n");
            out.write("            <div id=\"fragment-1\">\r\n");
            out.write("                <form action=\"\">\r\n");
            out.write("                </form>\r\n");
            out.write("                <p>First tab is active by default:</p>\r\n");
            out.write("                <pre><code>$('#example > ul').tabs();</code></pre>\r\n");
            out.write("\r\n");
            out.write("            </div>\r\n");
            out.write("            <div id=\"fragment-2\">\r\n");
            out.write("                Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.\r\n");
            out.write("                Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.\r\n");
            out.write("            </div>\r\n");
            out.write("            <div id=\"fragment-3\">\r\n");
            out.write("                Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.\r\n");
            out.write("                Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.\r\n");
            out.write("                Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.\r\n");
            out.write("            </div>\r\n");
            out.write("        </div>\r\n");
            out.write("</body>\r\n");
            out.write("</html>");
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
