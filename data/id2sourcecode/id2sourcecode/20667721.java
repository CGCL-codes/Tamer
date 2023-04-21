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
            response.setContentType("text/html");
            pageContext = _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);
            _jspx_page_context = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _jspx_out = out;
            out.write("\r\n\r\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\r\n<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\r\n<head>\r\n  <title>satmule status</title>\r\n  <link title=\"satmule_style\" rel=\"stylesheet\" href=\"css/styles.css\" type=\"text/css\" />\r\n</head>\r\n<body class=\"bgpage\">\r\n");
            out.write("<table style=\"width: 100%;\" cellpadding=\"2\" cellspacing=\"2\">\r\n  <tbody>\r\n    <tr>\r\n      <th class=\"headtable\"><a href=\"c?op=listDownloads&amp;orderBy=FILEHASH&amp;page=0\">Files</a></th>\r\n      <th class=\"headtable\"><a href=\"c?op=showMemory\">Status</a></th>\r\n      <th class=\"headtable\"><a href=\"downloadRate.jsp\">Download Rate</a></th>\r\n    </tr>\r\n  </tbody>\r\n</table>\r\n");
            out.write("\r\n\r\n   <form name=\"newTcpFileReader\" action=c>\r\n   <input type=\"hidden\" name=\"op\" value=\"newTcpFileReader\" />\r\n\r\n<table align=\"center\" class=\"bgtable\">\r\n  <tbody>\r\n    <tr>\r\n      <th class=\"headtable\" colspan=\"2\" rowspan=\"1\">java\r\nvirtual machine</th>\r\n    </tr>\r\n    <tr>\r\n      <td class=\"headtable\">max memory</td>\r\n      <td>");
            out.print(java.lang.Runtime.getRuntime().maxMemory() / 1000);
            out.write("</td>\r\n    </tr>\r\n    <tr>\r\n      <td class=\"headtable\">total memory</td>\r\n      <td> ");
            out.print(java.lang.Runtime.getRuntime().totalMemory() / 1000);
            out.write("</td>\r\n    </tr>\r\n    <tr>\r\n      <td class=\"headtable\">free memory</td>\r\n      <td>");
            out.print(java.lang.Runtime.getRuntime().freeMemory() / 1000);
            out.write("</td>\r\n    </tr>\r\n    <tr class=\"headtable\">\r\n      <th colspan=\"2\" rowspan=\"1\">satmule</th>\r\n    </tr>\r\n    <tr>\r\n      <td class=\"headtable\">active comunication sockets</td>\r\n      <td>");
            out.print(Context.getTcpManager().getTcpThreads().size());
            out.write("</td>\r\n    </tr>\r\n    <tr>\r\n      <td class=\"headtable\">tcp buffer ocupation</td>\r\n      <td>");
            out.print(Context.getTcpBuffer().getOcupation());
            out.write(' ');
            out.write('(');
            out.print(Context.getTcpBuffer().getOcupation() * 100 / Context.getTcpBuffer().getMaxSize());
            out.write("%)</td>\r\n    </tr>\r\n    <tr>\r\n      <td class=\"headtable\">chunk buffer ocupation</td>\r\n      <td>");
            out.print(Context.getChunkBuffer().getOcupation());
            out.write(' ');
            out.write('(');
            out.print(Context.getChunkBuffer().getOcupation() * 100 / Context.getChunkBuffer().getMaxSize());
            out.write("%)</td>\r\n    </tr>\r\n    <tr>\r\n      <td class=\"headtable\">filename buffer ocupation</td>\r\n      <td>");
            out.print(Context.getFileNameBuffer().getOcupation());
            out.write(' ');
            out.write('(');
            out.print(Context.getFileNameBuffer().getOcupation() * 100 / Context.getFileNameBuffer().getMaxSize());
            out.write("%)</td>\r\n    </tr>\r\n    <tr>\r\n      <td class=\"headtable\">current files in cache</td>\r\n      <td>");
            out.print(EFileCache.getInstance().numFiles());
            out.write("</td>\r\n    </tr>\r\n    <tr class=\"headtable\">\r\n      <th colspan=\"2\">tcp capturers</th>\r\n    </tr>\r\n    <tr>\r\n      <td class=\"headtable\">direct readers</td>\r\n      <td>skynet</td>\r\n    </tr>\r\n    \r\n    <tr>\r\n      <td class=\"headtable\">tcp file reader</td>\r\n      <td>");
            out.print(Context.getTcpFileReaders().size());
            out.write("</td>\r\n    </tr>\r\n\t");
            Vector fileReaders = Context.getTcpFileReaders();
            for (int f = 0; f < fileReaders.size(); f++) {
                FileReaderTcpServer fr = (FileReaderTcpServer) (fileReaders.elementAt(f));
                out.write("\r\n    <tr>\r\n      <td class=\"headtable\">tcp file reader</td>\r\n      <td>");
                out.print(fr.getFileName());
                out.write(' ');
                out.print(fr.getTreader().isAlive());
                out.write("</td>\r\n    </tr>\r\n    \r\n    ");
            }
            out.write("\r\n    <tr>\r\n      <td class=\"headtable\">new tcp file for input</td>\r\n      <td><input type=\"text\" name=\"filename\" value=\"\" /><input type=\"submit\" name=\"GO\" value=\"GO\" /></td>\r\n    </tr>\r\n     \r\n    <tr>\r\n      <td class=\"headtable\">tcp server</td>\r\n      <td>waiting connections on port ");
            out.print("8080");
            out.write("</td>\r\n    </tr>\r\n    <tr class=\"headtable\">\r\n      <th colspan=\"2\" rowspan=\"1\">ed2k client</th>\r\n    </tr>\r\n    <tr>\r\n      <td class=\"headtable\">edonkey client</td>\r\n      <td>state:\r\n    ");
            if (Context.getInfoSearcher().isConnected()) {
                out.write("\r\n     connected <a href=\"c?op=disconnectClient\">disconnect</a>\r\n    ");
            } else {
                out.write("\r\n     disconnected <a href=\"c?op=connectClient\">connect</a>\r\n    ");
            }
            out.write("\r\n      </td>\r\n    </tr>\r\n    <tr>\r\n      <td class=\"headtable\">address</td>\r\n      <td>");
            out.print(Context.getHost());
            out.write("</td>\r\n    </tr>\r\n    <tr>\r\n      <td class=\"headtable\">port</td>\r\n      <td>");
            out.print(Context.getPort());
            out.write("</td>\r\n    </tr>\r\n  </tbody>\r\n</table>\r\n\r\n</form>\r\n\r\n</body>\r\n</html>\r\n\r\n");
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
