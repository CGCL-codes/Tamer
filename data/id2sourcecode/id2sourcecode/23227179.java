    public org.apache.struts.action.ActionForward execute(org.apache.struts.action.ActionMapping actionMapping, org.apache.struts.action.ActionForm actionForm, javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse) {
        aportal.form.miscellaneous.TopStoriesForm tsf = (aportal.form.miscellaneous.TopStoriesForm) actionForm;
        javax.servlet.http.HttpSession session = httpServletRequest.getSession();
        Object libraryIdObj = session.getAttribute("LibraryId");
        String forwardname = "";
        if (libraryIdObj == null) {
            forwardname = "start";
        } else {
            forwardname = "thisPage";
            try {
                String libraryId = libraryIdObj.toString();
                org.jdom.Element elex = new org.jdom.Element("OperationId");
                org.jdom.Element libele = new org.jdom.Element("LibraryID");
                libele.setText(libraryId);
                elex.addContent(libele);
                org.jdom.Document doc = new org.jdom.Document();
                doc.setRootElement(elex);
                String xml = (new org.jdom.output.XMLOutputter()).outputString(doc);
                String resultxml = ((ejb.bprocess.administration.TopStoriesRSSFeedSessionHome) ejb.bprocess.util.HomeFactory.getInstance().getRemoteHome("TopStoriesRSSFeedSession")).create().getDetails(xml);
                java.util.Hashtable htFeeds = new java.util.Hashtable();
                org.jdom.input.SAXBuilder sb = new org.jdom.input.SAXBuilder();
                org.jdom.Document retdoc = null;
                try {
                    retdoc = sb.build(new java.io.StringReader(resultxml));
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
                if (retdoc != null) {
                    java.util.List liids = retdoc.getRootElement().getChildren();
                    for (int i = 0; i < liids.size(); i++) {
                        org.jdom.Element eleone = (org.jdom.Element) liids.get(i);
                        String id = eleone.getChildTextTrim("RssId");
                        String name = eleone.getChildTextTrim("RssFeedName");
                        String location = eleone.getChildTextTrim("RssFeedLocation");
                        java.util.Vector vecFe = new java.util.Vector(1, 1);
                        vecFe.addElement(name);
                        vecFe.addElement(location);
                        htFeeds.put(id, vecFe);
                    }
                }
                java.util.Enumeration enumFe = htFeeds.keys();
                while (enumFe.hasMoreElements()) {
                    String id = enumFe.nextElement().toString();
                    java.util.Vector vecFe = (java.util.Vector) htFeeds.get(id);
                    String location = vecFe.elementAt(1).toString();
                    try {
                        java.net.URL url = new java.net.URL(location);
                        java.net.URLConnection urlcon = url.openConnection();
                        if (ProxySettings.getInstance().isProxyAvailable()) {
                            urlcon.setRequestProperty("Proxy-Authorization", "Basic " + ProxySettings.getInstance().getEncodedPassword());
                        }
                        urlcon.setDoInput(true);
                        java.io.InputStream is = urlcon.getInputStream();
                        org.jdom.input.SAXBuilder sb1 = new org.jdom.input.SAXBuilder();
                        org.jdom.Document doc1 = sb1.build(is);
                        org.jdom.Element eleChan = doc1.getRootElement().getChild("channel");
                        String chanTitle = eleChan.getChildTextTrim("title");
                        String chanlink = eleChan.getChildTextTrim("link");
                        String chandesc = eleChan.getChildTextTrim("description");
                        String pubDate = eleChan.getChildTextTrim("pubDate");
                        String copyright = eleChan.getChildTextTrim("copyright");
                        if (chanTitle == null) vecFe.addElement(""); else vecFe.addElement(chanTitle);
                        if (chanlink == null) vecFe.addElement(""); else vecFe.addElement(chanlink);
                        if (chandesc == null) vecFe.addElement(""); else vecFe.addElement(chandesc);
                        if (pubDate == null) vecFe.addElement(""); else vecFe.addElement(pubDate);
                        if (copyright == null) vecFe.addElement(""); else vecFe.addElement(copyright);
                        java.util.List liitem = eleChan.getChildren("item");
                        java.util.Vector vecitems = new java.util.Vector(1, 1);
                        for (int j = 0; j < liitem.size(); j++) {
                            org.jdom.Element eleitem = (org.jdom.Element) liitem.get(j);
                            String[] strx = new String[3];
                            java.util.Vector vecx = new java.util.Vector();
                            vecx.addElement(eleitem.getChildTextTrim("title"));
                            vecx.addElement(eleitem.getChildTextTrim("link"));
                            vecx.addElement(eleitem.getChildTextTrim("description"));
                            vecitems.addElement(vecx);
                        }
                        vecFe.addElement(vecitems);
                        is.close();
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
                    htFeeds.put(id, vecFe);
                }
                tsf.setRssFeed(htFeeds);
                System.out.println(htFeeds);
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
        return actionMapping.findForward(forwardname);
    }
