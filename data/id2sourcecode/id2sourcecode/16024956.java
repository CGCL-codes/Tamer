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
            out.write("\r\n");
            out.write("<html>\r\n");
            out.write("<head>\r\n");
            out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n");
            out.write("<title>中山市美斯特实业有限公司</title>\r\n");
            out.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"/system/css/style.css\" />\r\n");
            out.write("<script type=\"text/javascript\" language=\"javascript\" src=\"/system/js/public.js\"></script>\r\n");
            out.write("<script type=\"text/javascript\" src=\"../js/jquery-1.4.2.js\"></script>\r\n");
            out.write("<script type=\"text/javascript\" src=\"../js/validate.js\"></script>\r\n");
            out.write("<script type=\"text/javascript\">\r\n");
            out.write("\t\t//验证按钮名是否为空\r\n");
            out.write("\t\t$(document).ready(function(){\t\t\r\n");
            out.write("\t\t\t\tvalidateIsNull(\"#menuName\",\"[菜单名不能为空！]\");\r\n");
            out.write("\t\t\t\tvalidateNumber(\"#menuSort\",\"排序非法，要求是正整数\");\t\t\t\t\t\r\n");
            out.write("\t\t\t\tvalidateIsNull(\"#actionKey\",\"[actionKey不能为空！]\");\r\n");
            out.write("\t\t\t\tvar old_actionKey=$(\"#actionKey\").val();\r\n");
            out.write("\t\t\t\t$(\"#actionKey\").change(function(){\r\n");
            out.write("\t\t\t\t  \t\tvar actionKey=$(\"#actionKey\").val();\r\n");
            out.write("\t\t\t\t  \t\tif(actionKey==old_actionKey) return false;\r\n");
            out.write("\t\t\t\t  \t\tajaxRequest_validate(\"#actionKey\",\"/system/ajax/menu.do?method=validate\",{\"actionKey\":actionKey});\r\n");
            out.write("\t\t\t\t});\t\t\r\n");
            out.write("\t\r\n");
            out.write("\t\t\t});\t\r\n");
            out.write("</script>\r\n");
            out.write("\r\n");
            out.write("</head>\r\n");
            out.write("<body>\r\n");
            out.write("<div class=\"main\">\r\n");
            out.write("\t<div class=\"position\">当前位置: <a href=\"/sysadm/desktop.jsp\">桌 面</a> → 添加菜单</div>\r\n");
            out.write("\t<div class=\"mainbody\">\r\n");
            out.write("\t\t<div class=\"operate_info\">操作说明：带 * 号必填</div>\r\n");
            out.write("\t\t<div class=\"table\">\r\n");
            out.write("\t\t<form action=\"/system/config/menu.do?method=update\" method=\"post\" onsubmit=\"return checkForm()\">\r\n");
            out.write("\t\t\t<table width=\"100%\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" class=\"table_form\">\r\n");
            out.write("\t\t\t");
            com.zhongkai.web.tag.ListTag _jspx_th_mytag_005fList_005f0 = (com.zhongkai.web.tag.ListTag) _005fjspx_005ftagPool_005fmytag_005fList_0026_005ftable_005fname.get(com.zhongkai.web.tag.ListTag.class);
            _jspx_th_mytag_005fList_005f0.setPageContext(_jspx_page_context);
            _jspx_th_mytag_005fList_005f0.setParent(null);
            _jspx_th_mytag_005fList_005f0.setTable("Menu");
            _jspx_th_mytag_005fList_005f0.setName("menuList");
            int _jspx_eval_mytag_005fList_005f0 = _jspx_th_mytag_005fList_005f0.doStartTag();
            if (_jspx_eval_mytag_005fList_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_mytag_005fList_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                    out = _jspx_page_context.pushBody();
                    _jspx_th_mytag_005fList_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                    _jspx_th_mytag_005fList_005f0.doInitBody();
                }
                do {
                    out.write("menuId=");
                    out.print(request.getParameter("menu_id"));
                    int evalDoAfterBody = _jspx_th_mytag_005fList_005f0.doAfterBody();
                    if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
                } while (true);
                if (_jspx_eval_mytag_005fList_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                    out = _jspx_page_context.popBody();
                }
            }
            if (_jspx_th_mytag_005fList_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fmytag_005fList_0026_005ftable_005fname.reuse(_jspx_th_mytag_005fList_005f0);
                return;
            }
            _005fjspx_005ftagPool_005fmytag_005fList_0026_005ftable_005fname.reuse(_jspx_th_mytag_005fList_005f0);
            out.write("\r\n");
            out.write("\t\t\t\t\t");
            org.apache.struts.taglib.logic.IterateTag _jspx_th_logic_005fiterate_005f0 = (org.apache.struts.taglib.logic.IterateTag) _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.get(org.apache.struts.taglib.logic.IterateTag.class);
            _jspx_th_logic_005fiterate_005f0.setPageContext(_jspx_page_context);
            _jspx_th_logic_005fiterate_005f0.setParent(null);
            _jspx_th_logic_005fiterate_005f0.setId("update_menu");
            _jspx_th_logic_005fiterate_005f0.setName("menuList");
            int _jspx_eval_logic_005fiterate_005f0 = _jspx_th_logic_005fiterate_005f0.doStartTag();
            if (_jspx_eval_logic_005fiterate_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                java.lang.Object update_menu = null;
                if (_jspx_eval_logic_005fiterate_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                    out = _jspx_page_context.pushBody();
                    _jspx_th_logic_005fiterate_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                    _jspx_th_logic_005fiterate_005f0.doInitBody();
                }
                update_menu = (java.lang.Object) _jspx_page_context.findAttribute("update_menu");
                do {
                    out.write("\r\n");
                    out.write("\t\t\t\t\t<input type=\"hidden\" name=\"menuId\" value=\"");
                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${update_menu.menuId}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                    out.write("\">\r\n");
                    out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
                    out.write("\t\t\t\t\t\t<td align=\"right\">菜单名称：</td>\r\n");
                    out.write("\t\t\t\t\t\t<td><input name=\"menuName\" id=\"menuName\" value=\"");
                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${update_menu.menuName}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                    out.write("\"/>　*必填 </td>\r\n");
                    out.write("\t\t\t\t\t</tr>\r\n");
                    out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
                    out.write("\t\t\t\t\t\t<td align=\"right\">排序：</td>\r\n");
                    out.write("\t\t\t\t\t\t<td><input name=\"update_menuSort\" value=\"");
                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${update_menu.menuSort}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                    out.write("\" id=\"menuSort\" size=\"5\" />　*必填 默认 0 </td>\r\n");
                    out.write("\t\t\t\t\t</tr>\r\n");
                    out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
                    out.write("\t\t\t\t\t\t<td align=\"right\">链接地址：</td>\r\n");
                    out.write("\t\t\t\t\t\t<td><input name=\"menuUrl\" value=\"");
                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${update_menu.menuUrl}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                    out.write("\"/>　非必填 例如：addform.jsp </td>\r\n");
                    out.write("\t\t\t\t\t</tr>\r\n");
                    out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
                    out.write("\t\t\t\t\t\t<td align=\"right\">操作键：</td>\r\n");
                    out.write("\t\t\t\t\t\t<td><input name=\"actionKey\" id=\"actionKey\" value=\"");
                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${update_menu.actionKey}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                    out.write("\"/>　*必填 例如：adduser </td>\r\n");
                    out.write("\t\t\t\t\t</tr>\r\n");
                    out.write("\t\t\t\t\t");
                    int evalDoAfterBody = _jspx_th_logic_005fiterate_005f0.doAfterBody();
                    update_menu = (java.lang.Object) _jspx_page_context.findAttribute("update_menu");
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
            out.write("\t\t\t\t\t\r\n");
            out.write("\t\t\t\t\t<tr>\r\n");
            out.write("\t\t\t\t\t\t<td colspan=\"2\" class=\"form_button\" style=\"padding-top:10px;\">\r\n");
            out.write("\t\t\t\t\t\t\t<input type=\"submit\" value=\"更新\" />\r\n");
            out.write("\t\t\t\t\t\t\t<input type=\"button\" value=\"返回\" onClick=\"location.href='menulist.jsp'\" />\r\n");
            out.write("\t\t\t\t\t\t</td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t</table>\r\n");
            out.write("\t\t\t</form>\r\n");
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
