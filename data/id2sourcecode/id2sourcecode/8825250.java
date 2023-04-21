    private void writeFieldsHookScript() throws JspTagException {
        if (dateFieldsIDs.isEmpty() && !hasIntegerFields && !hasDecimalFields) {
            return;
        }
        final String contextPath = ((HttpServletRequest) this.pageContext.getRequest()).getContextPath();
        write("<script type=\"text/javascript\" encoding=\"ISO-8859-1\"");
        write("src=\"" + contextPath + "/scripts/ui.datepicker.js\"");
        writeLine("></script>");
        writeLine("<script type='text/javascript' encoding=\"ISO-8859-1\">");
        writeLine("	$(document).ready(function(){");
        for (String id : dateFieldsIDs) {
            writeLine("		$('#" + id + "').datepicker();");
        }
        if (hasIntegerFields) {
            writeLine("$(\".digits\").keypress(function (e){");
            writeLine(" if( e.which!=8 && e.which!=0 && (e.which<48 || e.which>57)){");
            writeLine("	return false;");
            writeLine(" }");
            writeLine("});");
        }
        if (hasDecimalFields) {
            writeLine("$(\".number\").keypress(function (e){");
            writeLine(" if( e.which!=8 && e.which!=0 && (e.which<48 || e.which>57)){");
            writeLine("	return false;");
            writeLine(" }");
            writeLine("});");
        }
        writeLine("	});");
        writeLine("</script>");
    }
