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
            response.setContentType("text/html; charset=utf-8");
            pageContext = _jspxFactory.getPageContext(this, request, response, "", true, 8192, true);
            _jspx_page_context = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _jspx_out = out;
            out.write('\r');
            out.write('\n');
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            pageContext.setAttribute("basePath", basePath);
            out.write("\r\n");
            out.write("<html>\r\n");
            out.write("<head>\r\n");
            out.write("<base href=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageScope.basePath }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\">\r\n");
            out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n");
            out.write("<title>中山市中凯信息科技有限公司</title>\r\n");
            out.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"system/css/style.css\" />\r\n");
            out.write("<link href=\"system/plugins/validateMyForm/css/plugin.css\" rel=\"stylesheet\" type=\"text/css\">\r\n");
            out.write("<script type=\"text/javascript\" src=\"system/js/jquery-1.4.2.js\"></script>\r\n");
            out.write("<script type=\"text/javascript\" src=\"system/js/verify1.js\"></script>\r\n");
            out.write("<script type=\"text/javascript\" src=\"system/plugins/validateMyForm/js/jquery.validateMyForm.1.5.js\"></script>\r\n");
            out.write("<script type=\"text/javascript\" language=\"javascript\" src=\"system/js/public.js\"></script>\r\n");
            out.write("</head>\r\n");
            out.write("<body>\r\n");
            out.write("<div class=\"main\">\r\n");
            out.write("\t<div class=\"position\">当前位置: <a href=\"sysadm/desktop.jsp\">桌 面</a> → 用户信息中心</div>\r\n");
            out.write("\t<div class=\"mainbody\">\r\n");
            out.write("\t\t<div class=\"operate_info\">\r\n");
            out.write("\t\t\t未阅读的信息：<br/>\t\t\r\n");
            out.write("\t\t</div>\r\n");
            out.write("\t\t<div class=\"table\">\t\r\n");
            out.write("\t\t\t\t\r\n");
            out.write("\t\t\t<table width=\"100%\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" class=\"table_list\">\t\t\t\t\r\n");
            out.write("\t\t\t\t<tr>\r\n");
            out.write("\t\t\t\t\t<th>发件人</th>\r\n");
            out.write("\t\t\t\t\t<th>标题</th>\r\n");
            out.write("\t\t\t\t\t<th>内容</th>\r\n");
            out.write("\t\t\t\t\t<th>信息状态</th>\t\t\t\t\t\r\n");
            out.write("\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t");
            if (_jspx_meth_mytag_005fList_005f0(_jspx_page_context)) return;
            out.write("\t\t\t\r\n");
            out.write("\t\t\t\t");
            org.apache.struts.taglib.logic.NotEmptyTag _jspx_th_logic_005fnotEmpty_005f0 = (org.apache.struts.taglib.logic.NotEmptyTag) _005fjspx_005ftagPool_005flogic_005fnotEmpty_0026_005fname.get(org.apache.struts.taglib.logic.NotEmptyTag.class);
            _jspx_th_logic_005fnotEmpty_005f0.setPageContext(_jspx_page_context);
            _jspx_th_logic_005fnotEmpty_005f0.setParent(null);
            _jspx_th_logic_005fnotEmpty_005f0.setName("newMsgs");
            int _jspx_eval_logic_005fnotEmpty_005f0 = _jspx_th_logic_005fnotEmpty_005f0.doStartTag();
            if (_jspx_eval_logic_005fnotEmpty_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                    out.write("\t\t\t\r\n");
                    out.write("\t\t\t\t\t");
                    org.apache.struts.taglib.logic.IterateTag _jspx_th_logic_005fiterate_005f0 = (org.apache.struts.taglib.logic.IterateTag) _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.get(org.apache.struts.taglib.logic.IterateTag.class);
                    _jspx_th_logic_005fiterate_005f0.setPageContext(_jspx_page_context);
                    _jspx_th_logic_005fiterate_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_005fnotEmpty_005f0);
                    _jspx_th_logic_005fiterate_005f0.setId("msg");
                    _jspx_th_logic_005fiterate_005f0.setName("newMsgs");
                    int _jspx_eval_logic_005fiterate_005f0 = _jspx_th_logic_005fiterate_005f0.doStartTag();
                    if (_jspx_eval_logic_005fiterate_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        java.lang.Object msg = null;
                        if (_jspx_eval_logic_005fiterate_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                            out = _jspx_page_context.pushBody();
                            _jspx_th_logic_005fiterate_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                            _jspx_th_logic_005fiterate_005f0.doInitBody();
                        }
                        msg = (java.lang.Object) _jspx_page_context.findAttribute("msg");
                        do {
                            out.write("\t\t\t\t\t\t\t\t\t\t\t\r\n");
                            out.write("\t\t\t\t\t<tr>\t\t\t\t\t\t\t\t\t\r\n");
                            out.write("\t\t\t\t\t   ");
                            if (_jspx_meth_mytag_005fviewCol_005f0(_jspx_th_logic_005fiterate_005f0, _jspx_page_context)) return;
                            out.write("\r\n");
                            out.write("\t\t\t\t\t\t<td>");
                            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${user.userAccount}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                            out.write("</td>\r\n");
                            out.write("\t\t\t\t\t\t<td>");
                            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${msg.xxbt }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                            out.write("</td>\r\n");
                            out.write("\t\t\t\t\t\t<td>");
                            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${msg.xxnr}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                            out.write("</td>\r\n");
                            out.write("\t\t\t\t\t\t<td><a href=\"system/message/read_message.do?xh=");
                            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${msg.xh}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                            out.write("\">\r\n");
                            out.write("\t\t\t\t\t\t\t");
                            if (_jspx_meth_c_005fif_005f0(_jspx_th_logic_005fiterate_005f0, _jspx_page_context)) return;
                            out.write("\r\n");
                            out.write("\t\t\t\t\t\t\t");
                            if (_jspx_meth_c_005fif_005f1(_jspx_th_logic_005fiterate_005f0, _jspx_page_context)) return;
                            out.write("\r\n");
                            out.write("\t\t\t\t\t\t\t\r\n");
                            out.write("\t\t\t\t\t\t\t \r\n");
                            out.write("\t\t\t\t\t\t</a></td>\r\n");
                            out.write("\t\t\t\t\t\r\n");
                            out.write("\t\t\t\t\t</tr>\r\n");
                            out.write("\t\t\t\t\t");
                            int evalDoAfterBody = _jspx_th_logic_005fiterate_005f0.doAfterBody();
                            msg = (java.lang.Object) _jspx_page_context.findAttribute("msg");
                            if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
                        } while (true);
                        if (_jspx_eval_logic_005fiterate_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                            out = _jspx_page_context.popBody();
                        }
                    }
                    if (_jspx_th_logic_005fiterate_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.reuse(_jspx_th_logic_005fiterate_005f0);
                        return;
                    }
                    _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.reuse(_jspx_th_logic_005fiterate_005f0);
                    out.write("\r\n");
                    out.write("\t\t\t\t");
                    int evalDoAfterBody = _jspx_th_logic_005fnotEmpty_005f0.doAfterBody();
                    if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
                } while (true);
            }
            if (_jspx_th_logic_005fnotEmpty_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005flogic_005fnotEmpty_0026_005fname.reuse(_jspx_th_logic_005fnotEmpty_005f0);
                return;
            }
            _005fjspx_005ftagPool_005flogic_005fnotEmpty_0026_005fname.reuse(_jspx_th_logic_005fnotEmpty_005f0);
            out.write("\t\t\t\r\n");
            out.write("\t\t\t\t");
            if (_jspx_meth_logic_005fempty_005f0(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\t\t\t\t\t\t\t\t\r\n");
            out.write("\t\t\t</table>\r\n");
            out.write("\t\t</div>\r\n");
            out.write("\t</div>\r\n");
            out.write("</div>\r\n");
            out.write("</body>\r\n");
            out.write("</html>\r\n");
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
