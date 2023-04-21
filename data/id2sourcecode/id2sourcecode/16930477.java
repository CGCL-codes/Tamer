    public int doEndTag() throws JspException {
        LinkedList<Page> result = new LinkedList<Page>();
        Page current = getCurrentPage();
        while (current != null) {
            result.addFirst(current);
            current = current.getParent();
        }
        JspWriter out = pageContext.getOut();
        try {
            out.write("<div class='breadcrumbs_area'>");
            out.write("<span class=\"breadcrumb_message\">" + myInitialText + "&nbsp;</span>");
            for (Page page : result) {
                out.write("<span class=\"breadcrumb_label\">");
                if (page.isContent()) {
                    out.write("<a href='" + page.getID() + ".page' title='" + page.getTitle() + "'>");
                }
                out.write(page.getLabel());
                if (page.isContent()) {
                    out.write("</a>");
                }
                out.write("</span>");
                out.write("&nbsp;<img src=\"" + SchemeManager.getSchemeManager().getSchemeFile(myImageName) + "\" title='Bread Crumb' alt='bread crumb' align=\"middle\" valign='top'/>&nbsp;\n");
            }
            out.write("</div>");
        } catch (IOException e) {
            throw new JspException(e);
        } catch (PresentationException e) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }
