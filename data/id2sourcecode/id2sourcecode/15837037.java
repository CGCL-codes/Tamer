    private boolean _jspx_meth_ae_IfTrue_5(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.activebpel.rt.bpeladmin.war.tags.AeIfTrueTag _jspx_th_ae_IfTrue_5 = (org.activebpel.rt.bpeladmin.war.tags.AeIfTrueTag) _jspx_tagPool_ae_IfTrue_property_name.get(org.activebpel.rt.bpeladmin.war.tags.AeIfTrueTag.class);
        _jspx_th_ae_IfTrue_5.setPageContext(_jspx_page_context);
        _jspx_th_ae_IfTrue_5.setParent(null);
        _jspx_th_ae_IfTrue_5.setName("configBean");
        _jspx_th_ae_IfTrue_5.setProperty("internalWorkManager");
        int _jspx_eval_ae_IfTrue_5 = _jspx_th_ae_IfTrue_5.doStartTag();
        if (_jspx_eval_ae_IfTrue_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
                out.write("\r\n");
                out.write("         <tr>\r\n");
                out.write("           <td class=\"labelHeaders\" align=\"left\" nowrap=\"true\" width=\"20%\">&nbsp;");
                if (_jspx_meth_ae_GetResource_10(_jspx_th_ae_IfTrue_5, _jspx_page_context)) return true;
                out.write("&nbsp;</td>\r\n");
                out.write("           <td align=\"left\" colspan=\"2\"><input type=\"text\" cols=\"5\" tabIndex=\"8\" name=\"ec_thread_min\" value='");
                out.write(org.apache.jasper.runtime.JspRuntimeLibrary.toString((((org.activebpel.rt.bpeladmin.war.web.AeEngineConfigBean) _jspx_page_context.findAttribute("configBean")).getThreadPoolMin())));
                out.write("'/></td>\r\n");
                out.write("         </tr>\r\n");
                out.write("         <tr height=\"1\">\r\n");
                out.write("           <td colspan=\"3\" height=\"1\" class=\"tabular\"></td>\r\n");
                out.write("         </tr>\r\n");
                out.write("         <tr>\r\n");
                out.write("           <td class=\"labelHeaders\" align=\"left\" nowrap=\"true\" width=\"20%\">&nbsp;");
                if (_jspx_meth_ae_GetResource_11(_jspx_th_ae_IfTrue_5, _jspx_page_context)) return true;
                out.write("&nbsp;</td>\r\n");
                out.write("           <td align=\"left\" colspan=\"2\"><input type=\"text\" cols=\"5\" tabIndex=\"9\" name=\"ec_thread_max\" value='");
                out.write(org.apache.jasper.runtime.JspRuntimeLibrary.toString((((org.activebpel.rt.bpeladmin.war.web.AeEngineConfigBean) _jspx_page_context.findAttribute("configBean")).getThreadPoolMax())));
                out.write("'/></td>\r\n");
                out.write("         </tr>\r\n");
                out.write("         <tr height=\"1\">\r\n");
                out.write("            <td colspan=\"3\" height=\"1\" class=\"tabular\"></td>\r\n");
                out.write("         </tr>\r\n");
                out.write("      ");
                int evalDoAfterBody = _jspx_th_ae_IfTrue_5.doAfterBody();
                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
            } while (true);
        }
        if (_jspx_th_ae_IfTrue_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _jspx_tagPool_ae_IfTrue_property_name.reuse(_jspx_th_ae_IfTrue_5);
            return true;
        }
        _jspx_tagPool_ae_IfTrue_property_name.reuse(_jspx_th_ae_IfTrue_5);
        return false;
    }
