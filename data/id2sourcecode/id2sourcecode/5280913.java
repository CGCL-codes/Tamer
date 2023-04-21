    public int doStartTag() throws JspTagException {
        JspWriter out = pageContext.getOut();
        java.util.Vector vecloc = new java.util.Vector(1, 1);
        java.util.Vector vecxml = new java.util.Vector(1, 1);
        try {
            this.setID(this.pageContext.getRequest().getParameter("id"));
            if (id.equals("") == false) {
                try {
                    vecloc = ((ejb.bprocess.administration.TopStoriesRSSFeedSessionHome) ejb.bprocess.util.HomeFactory.getInstance().getRemoteHome("TopStoriesRSSFeedSession")).create().getRssFeeds(id);
                    for (int i = 0; i < vecloc.size(); i++) {
                        String urlStr = vecloc.elementAt(i).toString();
                        System.out.println("urlst" + urlStr);
                        java.net.URL url = new java.net.URL("" + urlStr + "");
                        java.net.URLConnection urlc = url.openConnection();
                        System.out.println("eeeeeeeeeeeeeeeeeeeee" + "haiu");
                        urlc.setDoOutput(true);
                        System.out.println("tttttttttttttttttttttttttttt");
                        java.io.InputStream is = urlc.getInputStream();
                        System.out.println("wwwwwwwwwwwww" + is);
                        org.jdom.input.SAXBuilder sb = new org.jdom.input.SAXBuilder();
                        System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
                        org.jdom.Document doc = sb.build(is);
                        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                        System.out.println("dddddddddddddddddddd" + doc);
                        java.util.List item = doc.getRootElement().getChild("channel").getChildren("item");
                        System.out.println("llllllllllllllllllllllll" + item);
                        for (int j = 0; j < item.size(); j++) {
                            org.jdom.Element items = (org.jdom.Element) item.get(j);
                            java.util.List itemchildren = items.getChildren();
                            for (int k = 0; k < itemchildren.size(); k++) {
                                org.jdom.Element child = (org.jdom.Element) item.get(k);
                                vecxml.addElement(child.getText());
                                out.close();
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            pageContext.getOut().write("<table>");
            for (int i = 0; i < vecxml.size(); i = +3) {
                String title = vecxml.elementAt(i).toString();
                String link = vecxml.elementAt(i + 1).toString();
                String description = vecxml.elementAt(i + 2).toString();
                pageContext.getOut().write("<tr>");
                pageContext.getOut().write("<td><a href=" + title + "</a></td>");
                pageContext.getOut().write("<td><a href=" + link + "</a></td>");
                pageContext.getOut().write("<td><a href=" + description + "</a></td>");
                pageContext.getOut().write("</tr>");
            }
            pageContext.getOut().write("</table>");
        } catch (Exception e) {
        }
        return SKIP_BODY;
    }
