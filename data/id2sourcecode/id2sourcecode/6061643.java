    protected javax.xml.transform.Source getSourceXml() throws java.net.MalformedURLException, org.jdom.JDOMException, java.io.IOException, org.xmldb.api.base.XMLDBException, com.skruk.elvis.db.DbException {
        java.net.URL url = null;
        javax.xml.transform.Source source = null;
        if (this.getXmlcol() == null) {
            if (this.getXml().startsWith("http")) {
                url = new java.net.URL(this.getXml());
                org.jdom.Document doc = new org.jdom.input.SAXBuilder().build(url);
                source = new javax.xml.transform.dom.DOMSource(new org.jdom.output.DOMOutputter().output(doc));
            } else {
                StringBuffer path = new StringBuffer(this.pageContext.getServletContext().getInitParameter("installDir"));
                path.append("/").append(this.getXml());
                url = new java.io.File(path.toString()).toURL();
                source = new javax.xml.transform.stream.StreamSource(url.openStream());
            }
        } else {
            DbEngine dbe = DbEngine.createInstance(this.pageContext.getServletContext());
            dbe.loadCollection(this.getXmlcol());
            XMLResource doc = dbe.getDocument(this.getXml());
            source = new javax.xml.transform.dom.DOMSource(doc.getContentAsDOM());
        }
        return source;
    }
