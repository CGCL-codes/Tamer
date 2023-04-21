    private void html(Writer wt, char[] content) throws JspException {
        RendererContext ctx = RendererContext.get();
        try {
            wt.write("<select name='" + getMangledName() + "'" + " id='" + ctx.idToExtId(getUniqueId()) + "'");
            if (ivTabIndex != null) wt.write(" tabindex='" + ivTabIndex.intValue() + "'");
            if (ivStyle != null) wt.write(" style='" + ivStyle + "'");
            if (ivClass != null) wt.write(" class='" + ivClass + "'");
            if (ivReadOnly) wt.write(" readonly='" + ivReadOnly + "'");
            if (ivMultiple) wt.write(" multiple='" + ivMultiple + "'");
            if (ivSize != null) wt.write(" size='" + ivSize.intValue() + "'");
            if (ivTitle != null) wt.write(" title='" + WEBUtil.string2Html(ivTitle) + "'");
            wt.write(">");
            if (content != null) wt.write(content); else if (getFieldWithChoices() != null) bindedFieldWithChoicesHtml(wt); else throw new IllegalStateException();
            wt.write("</select>\n");
            wt.write("<script>");
            wt.write("{");
            wt.write("var form=" + ivFormTag.getHtmlFormScriptAccessCode() + ";");
            wt.write("var subFormName='" + ivFormTag.getMangledName() + "';");
            wt.write("var comp=" + getClientScriptAccessCode() + ";");
            wt.write("{");
            script(wt);
            wt.write("}");
            if (ivBindedField != null) {
                IFieldDecorator fd = ctx.getFieldDecorator(ivBindedField);
                if (fd != null) {
                    wt.write("{");
                    fd.script(ivBindedField, wt);
                    wt.write("}");
                }
            }
            wt.write("}");
            wt.write("</script>");
        } catch (IOException ex) {
            throw new JspException(ex);
        }
    }
