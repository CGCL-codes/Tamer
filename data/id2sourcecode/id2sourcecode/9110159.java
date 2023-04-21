    public void _jspService(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
        JspFactory _jspxFactory = null;
        PageContext pageContext = null;
        HttpSession session = null;
        ServletContext application = null;
        ServletConfig config = null;
        JspWriter out = null;
        Object page = this;
        JspWriter _jspx_out = null;
        PageContext _jspx_page_context = null;
        try {
            _jspxFactory = JspFactory.getDefaultFactory();
            response.setContentType("text/html; charset=UTF-8");
            pageContext = _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);
            _jspx_page_context = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _jspx_out = out;
            out.write("\r\n");
            out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\r\n");
            out.write("\"http://www.w3.org/TR/html4/loose.dtd\">\r\n");
            out.write("<html>\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("   ");
            out.write("\r\n");
            out.write("   ");
            if (_jspx_meth_ae_RequestEncoding_0(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\r\n");
            out.write("   ");
            org.activebpel.rt.bpeladmin.war.web.AeDeploymentLogsBean logBean = null;
            synchronized (_jspx_page_context) {
                logBean = (org.activebpel.rt.bpeladmin.war.web.AeDeploymentLogsBean) _jspx_page_context.getAttribute("logBean", PageContext.PAGE_SCOPE);
                if (logBean == null) {
                    logBean = new org.activebpel.rt.bpeladmin.war.web.AeDeploymentLogsBean();
                    _jspx_page_context.setAttribute("logBean", logBean, PageContext.PAGE_SCOPE);
                    out.write("\r\n");
                    out.write("   ");
                }
            }
            out.write("\r\n");
            out.write("      \r\n");
            out.write("   ");
            org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "header_head.jsp", out, false);
            out.write("\r\n");
            out.write("   \r\n");
            out.write("   <body> \r\n");
            out.write("   \r\n");
            out.write("      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" align=\"center\">\r\n");
            out.write("         <tr>\r\n");
            out.write("            <td valign=\"top\" width=\"20%\">\r\n");
            out.write("            ");
            org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "header_nav.jsp", out, false);
            out.write("\r\n");
            out.write("            </td>\r\n");
            out.write("         \r\n");
            out.write("            <!-- spacer between nav and main -->\r\n");
            out.write("            <td width=\"3%\"></td>\r\n");
            out.write("         \r\n");
            out.write("            <td valign=\"top\">\r\n");
            out.write("               <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" align=\"left\">\r\n");
            out.write("                  <tr>\r\n");
            out.write("                     <th class=\"pageHeaders\" align=\"left\" nowrap=\"true\">&nbsp;");
            if (_jspx_meth_ae_GetResource_0(_jspx_page_context)) return;
            out.write("</th>\r\n");
            out.write("                  </tr>\r\n");
            out.write("                  <tr>\r\n");
            out.write("                    <td><textarea name=\"textarea\" style=\"width:99%; height:100%\" rows=\"25\" wrap=\"OFF\" readonly>");
            out.write(org.apache.jasper.runtime.JspRuntimeLibrary.toString((((org.activebpel.rt.bpeladmin.war.web.AeDeploymentLogsBean) _jspx_page_context.findAttribute("logBean")).getLogFile())));
            out.write("</textarea></td>\r\n");
            out.write("                  </tr>\r\n");
            out.write("               </table>\r\n");
            out.write("            </td>\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("            <!-- main and right margin       -->\r\n");
            out.write("            <td width=\"3%\"></td>\r\n");
            out.write("         </tr>\r\n");
            out.write("      </table>\r\n");
            out.write("   \r\n");
            out.write("      <br> \r\n");
            out.write("      ");
            org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "footer.jsp", out, false);
            out.write("\r\n");
            out.write("\r\n");
            out.write("   </body>\r\n");
            out.write("</html>\r\n");
        } catch (Throwable t) {
            if (!(t instanceof SkipPageException)) {
                out = _jspx_out;
                if (out != null && out.getBufferSize() != 0) out.clearBuffer();
                if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
            }
        } finally {
            if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
        }
    }
