    private boolean _jspx_meth_c_005fif_005f4(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f4 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
        _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f4.setParent(null);
        _jspx_th_c_005fif_005f4.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${task eq 'create' or task eq 'edit'}", java.lang.Boolean.class, (PageContext) _jspx_page_context, null, false)).booleanValue());
        int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
        if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
                out.write("\r\n");
                out.write("\t<fieldset><legend>Personendaten</legend>\r\n");
                out.write("\t<div class=\"type-text\"><label for=\"name\">Name:</label> <input\r\n");
                out.write("\t\tid=\"name\" name=\"name\" type=\"text\" size=\"30\" maxlength=\"30\"\r\n");
                out.write("\t\tvalue=\"");
                out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${customer.lastName}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                out.write("\" readonly=\"readonly\" /></div>\r\n");
                out.write("\t<div class=\"type-text\"><label for=\"firstname\">Vorname:</label> <input\r\n");
                out.write("\t\tid=\"firstname\" name=\"firstname\" type=\"text\" size=\"30\" maxlength=\"30\"\r\n");
                out.write("\t\tvalue=\"");
                out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${customer.firstName}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                out.write("\" readonly=\"readonly\" /></div>\r\n");
                out.write("\t</fieldset>\r\n");
                int evalDoAfterBody = _jspx_th_c_005fif_005f4.doAfterBody();
                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
            } while (true);
        }
        if (_jspx_th_c_005fif_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f4);
            return true;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f4);
        return false;
    }
