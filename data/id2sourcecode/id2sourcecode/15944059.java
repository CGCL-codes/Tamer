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
            out.write("<html>\r\n");
            out.write("<head>\r\n");
            out.write("\r\n");
            out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/layoutTecnica.css\" />\r\n");
            out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/fonts.css\" />\r\n");
            out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/menuCss.css\" />\r\n");
            out.write("<script language=\"JavaScript1.2\" type=\"text/javascript\"\r\n");
            out.write("\tsrc=\"css/menu.js\"></script>\r\n");
            out.write("\r\n");
            out.write("</head>\r\n");
            out.write("<body>\r\n");
            out.write("<div id=\"menu\">\r\n");
            out.write("<div id=\"topoEsq\"></div>\r\n");
            out.write("<div id=\"topoDir\"></div>\r\n");
            out.write("<div id=\"topoCentral\"></div>\r\n");
            out.write("<div id=\"colunaDir\"></div>\r\n");
            out.write("<div id=\"colunaEsq\"></div>\r\n");
            out.write("\t<div id=\"colunaCentral\" style=\" text-align:justify;\">\r\n");
            out.write("\t<div class=\"suckertreemenu\" align=\"center\">\r\n");
            out.write("\t<ul id=\"treemenu1\">\r\n");
            out.write("\t\t<li><a href=\"./principal.jsp\">Home</a>\r\n");
            out.write("\t\t<li><a href=\"javascript:;\">Projetos</a>\r\n");
            out.write("\t\t<ul>\r\n");
            out.write("\t\t\t<li><a href=\"./ListaMeusProjetosAction.do\">Meus Projetos</a></li>\r\n");
            out.write("\t\t\t<li><a href=\"./cadastrarProjeto.jsp\">Cadastrar</a></li>\r\n");
            out.write("\t\t\t<li><a href=\"./ListaRemoveAction.do\">Remover</a></li>\r\n");
            out.write("\t\t\t<li><a href=\"./atualizarProjeto.jsp\">Atualizar</a></li>\r\n");
            out.write("\t\t\t<li><a href=\"./pesquisarProjeto.jsp\">Pesquisar</a></li>\r\n");
            out.write("\t\t\t<li><a href=\"./ListarProjetosAssociaAction.do\">Associar</a></li>\r\n");
            out.write("\t\t\t<li><a href=\"./ListarRequisicoesAction.do\">Requisições</a></li>\r\n");
            out.write("\t\t</ul>\r\n");
            out.write("\t\t</li>\r\n");
            out.write("\t\t<li><a href=\"./LogoutAction.do\">Logout</a></li>\r\n");
            out.write("\t\r\n");
            out.write("\t</ul>\r\n");
            out.write("\t</div>\r\n");
            out.write("\t<br><br><br><br><br><br><br>\r\n");
            out.write("<title>Pesquisar Projeto</title>\r\n");
            out.write("<h1>Pesquisar</h1>\r\n");
            out.write("<br>\r\n");
            out.write("<form action=\"PesquisaAction.do\" method=\"POST\">\r\n");
            out.write("Nome do projeto: <input type=\"text\" name=\"nomeUnix\"/>\r\n");
            out.write("<input type=\"submit\" value=\"Pesquisar\"/>\r\n");
            out.write("</form>\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("<b>Nome Unix:</b> ");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${projeto.nomeUnix}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\r\n");
            out.write("<br>\r\n");
            out.write("<b>Nome Completo:</b> ");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${projeto.nomeCompleto}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\r\n");
            out.write("<br>\r\n");
            out.write("<b>Descricao:</b>\r\n");
            out.write("<textarea name=\"\" rows=4 cols=50 wrap=\"hard\" readonly>");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${projeto.descricao}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("</textarea>\r\n");
            out.write("<br>\r\n");
            out.write("<b>DescricaoPublica:</b>\r\n");
            out.write("<textarea name=\"\" rows=4 cols=50 wrap=\"hard\" readonly>");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${projeto.descricaoPublica}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("</textarea>\r\n");
            out.write("<br>\r\n");
            out.write("<b>Licenca:</b> ");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${projeto.licenca}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\r\n");
            out.write("<br>\r\n");
            out.write("<b>Status:</b> ");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${projeto.status}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\r\n");
            out.write("\r\n");
            out.write("</br></br>\r\n");
            out.write("<a href=\"./principal.jsp\"> <img src=\"imagens/voltar2.gif\" width=\"54\" height=\"19\"></a>\r\n");
            out.write("</div>\r\n");
            out.write("\t<div id=\"footer\" align=\"center\"> <img src=\"imagens/prinipal/barra embaixo.jpg\" ></div>\r\n");
            out.write("\t</div>\r\n");
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
