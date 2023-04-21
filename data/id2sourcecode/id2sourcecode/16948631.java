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
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("<script src=\"./js/geral.js\" type=\"text/javascript\"></script>\r\n");
            out.write("<script src=\"./js/jQuery.js\" type=\"text/javascript\"></script>\r\n");
            out.write("<script src=\"./js/calendar.js\" type=\"text/javascript\"></script>\r\n");
            out.write("<script src=\"./js/jquery.alphanumeric.pack.js\" type=\"text/javascript\"></script>\r\n");
            out.write("<script src=\"./js/jquery.tablesorter.js\" type=\"text/javascript\"></script>\r\n");
            out.write("<script src=\"./js/jquery.maskedinput.js\" type=\"text/javascript\"></script>\r\n");
            out.write("<script src=\"./js/linhaTabela.js\" type=\"text/javascript\"></script>\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("<script type=\"text/javascript\">\r\n");
            out.write("    function pesquisar(){\r\n");
            out.write("\t\tdocument.getElementById('metodo').value = \"pesquisar\";\r\n");
            out.write("\t\tdocument.forms[0].submit()\r\n");
            out.write("\t}\r\n");
            out.write("\t\r\n");
            out.write("\tfunction inicializar(){\r\n");
            out.write("\t\tif(");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${formListaCheque.tudoBranco}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("){\r\n");
            out.write("\t\t\tdocument.getElementById('listagem').style.display = 'none';\r\n");
            out.write("\t\t}else {\r\n");
            out.write("\t\t\tdocument.getElementById('listagem').style.display = 'block';\r\n");
            out.write("\t\t}\r\n");
            out.write("\t}\r\n");
            out.write("\t\r\n");
            out.write("\tfunction inserir(id){\r\n");
            out.write("\t\tdocument.getElementById('metodo').value = 'comentar';\r\n");
            out.write("\t\tdocument.getElementById('codigo').value = id;\r\n");
            out.write("\t\tvar just = document.getElementById('comentario');\r\n");
            out.write("\t\tjust.value = prompt(\"Insira o comentário do cheque:\",'");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${formPagamento.comentario}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("');\r\n");
            out.write("\t\tif(just.value != null && just.value != '') {\r\n");
            out.write("\t\t\tvar form = document.forms[0];\r\n");
            out.write("\t\t\tform.submit();\r\n");
            out.write("\t\t}\r\n");
            out.write("\t}\r\n");
            out.write("\r\n");
            out.write("\t\r\n");
            out.write("</script>\r\n");
            out.write("\r\n");
            out.write("<div id=\"corpo\">\r\n");
            out.write("\r\n");
            out.write("<div class=\"breadcrumb\">\r\n");
            out.write("\t");
            if (_jspx_meth_html_005flink_005f0(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\t\t&raquo;<a class=\"ativo\" href=\"#\">Listagem de Cheques</a> </div>\r\n");
            out.write("\r\n");
            if (_jspx_meth_html_005fform_005f0(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\r\n");
            out.write("<div id=\"listagem\">\r\n");
            out.write("<h3>Lista de recibos encontrados:</h3>\r\n");
            out.write("    \r\n");
            out.write("\t\t<p class=\"sub_titulo\"> </p>\r\n");
            out.write("\t\t<table width=\"70%\" class=\"posicaoTabela\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\r\n");
            out.write("\t\t\t\r\n");
            out.write("\t\t\t   \r\n");
            out.write("\t\t\t");
            if (_jspx_meth_display_005ftable_005f0(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\t\t \r\n");
            out.write("\t\t\t \r\n");
            out.write("\t\t</table>\r\n");
            out.write("</div>\r\n");
            out.write("</div>\r\n");
            out.write("\r\n");
            out.write("<script>\r\n");
            out.write("\tinicializar();\r\n");
            out.write("</script>");
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
