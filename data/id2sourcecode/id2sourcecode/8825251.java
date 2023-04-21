    private void writeValidationHookScript() throws JspTagException {
        final String contextPath = ((HttpServletRequest) this.pageContext.getRequest()).getContextPath();
        write("<script type=\"text/javascript\" encoding=\"ISO-8859-1\"");
        write("src=\"" + contextPath + "/scripts/jquery.validate.js\"");
        writeLine("></script>");
        write("<script type=\"text/javascript\" encoding=\"ISO-8859-1\"");
        write("src=\"" + contextPath + "/scripts/jquery.metadata.js\"");
        writeLine("></script>");
        writeLine("<script type=\"text/javascript\">");
        writeLine("	$(document).ready(function(){");
        writeLine("		$(\"#" + id + "\").validate();");
        writeLine("});");
        writeLine("</script>");
    }
