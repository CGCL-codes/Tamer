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
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            if (_jspx_meth_fmt_005fsetBundle_005f0(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("<div class=\"subcolumns equalize box-top\">\r\n");
            out.write("<div class=\"c33l\">\r\n");
            out.write("<div class=\"subcl\">\r\n");
            out.write("<h6>Tilgungsplan erstellen</h6>\r\n");
            out.write("<img width=\"180\" height=\"120\" border=\"0\" alt=\"\"\r\n");
            out.write("\tsrc=\"style/images/tilgungsplan_icon.png\" />\r\n");
            out.write("<p>Wir erstellen Ihnen einen Tilgungsplan, ganz nach Ihren Wünschen.</p>\r\n");
            out.write("<a href=\"#\" class=\"hideme\">&rarr; read more ...</a></div>\r\n");
            out.write("</div>\r\n");
            out.write("<div class=\"c33l\">\r\n");
            out.write("<div class=\"subc\">\r\n");
            out.write("<h6>Bonit&auml;t pr&uuml;fen</h6>\r\n");
            out.write("<img width=\"180\" height=\"120\" border=\"0\" alt=\"\"\r\n");
            out.write("\tsrc=\"style/images/bonitaet.jpg\" />\r\n");
            out.write("<p>Ob sich Ihre Ausgaben mit den Einnahmen decken, erfahren Sie\r\n");
            out.write("hier.</p>\r\n");
            out.write("<a href=\"#\" class=\"hideme\">&rarr; read more ...</a></div>\r\n");
            out.write("</div>\r\n");
            out.write("\r\n");
            out.write("<div class=\"c33r\">\r\n");
            out.write("<div class=\"subcr\">\r\n");
            out.write("<h6>Registrierung</h6>\r\n");
            out.write("<img width=\"180\" height=\"120\" border=\"0\" alt=\"\"\r\n");
            out.write("\tsrc=\"style/images/haushaltsdaten.jpg\" />\r\n");
            out.write("<p>Sie wollen auch schnell und einfach bares Geld in ihren Händen\r\n");
            out.write("halten? Dann zögern sie nicht und registrieren sie sich einfach bei uns.</p>\r\n");
            out.write("<a href=\"#\" class=\"hideme\">&rarr; read more ...</a></div>\r\n");
            out.write("</div>\r\n");
            out.write("</div>\r\n");
            out.write("<h3 class=\"hideme\">Summing up</h3>\r\n");
            out.write("<div class=\"subcolumns equalize no-ie-padding box-bottom\">\r\n");
            out.write("<div class=\"c33l\">\r\n");
            out.write("<div class=\"subcl\"><a href=\"index.html?c=redemption\"\r\n");
            out.write("\tclass=\"noprint\">&rarr; weiter<span class=\"hideme\"> about\r\n");
            out.write("Topic One</span></a></div>\r\n");
            out.write("</div>\r\n");
            out.write("<div class=\"c33l\">\r\n");
            out.write("<div class=\"subc\"><a href=\"index.html?c=solvency\" class=\"noprint\">&rarr;\r\n");
            out.write("weiter<span class=\"hideme\"> about Topic Two</span></a></div>\r\n");
            out.write("</div>\r\n");
            out.write("<div class=\"c33r\">\r\n");
            out.write("<div class=\"subcr\"><a\r\n");
            out.write("\thref=\"index.html?c=user&task=create");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${redirect}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\" class=\"noprint\">&rarr;\r\n");
            out.write("weiter<span class=\"hideme\"> about Topic Tree</span></a></div>\r\n");
            out.write("</div>\r\n");
            out.write("</div>\r\n");
            out.write("\r\n");
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
