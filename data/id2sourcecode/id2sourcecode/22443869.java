    @Override
    public String getString() {
        Element root = getElement("feed");
        WikiEngine engine = m_wikiContext.getEngine();
        Date lastModified = new Date(0L);
        for (Iterator i = m_entries.iterator(); i.hasNext(); ) {
            Entry e = (Entry) i.next();
            if (e.getPage().getLastModified().after(lastModified)) lastModified = e.getPage().getLastModified();
        }
        root.addContent(getElement("title").setText(getChannelTitle()));
        root.addContent(getElement("id").setText(getFeedID()));
        root.addContent(getElement("updated").setText(DateFormatUtils.formatUTC(lastModified, RFC3339FORMAT)));
        root.addContent(getElement("link").setAttribute("href", engine.getBaseURL()));
        root.addContent(getElement("generator").setText("JSPWiki " + Release.VERSTR));
        String rssFeedURL = engine.getURL(WikiContext.NONE, "rss.jsp", "page=" + engine.encodeName(m_wikiContext.getPage().getName()) + "&mode=" + m_mode + "&type=atom", true);
        Element self = getElement("link").setAttribute("rel", "self");
        self.setAttribute("href", rssFeedURL);
        root.addContent(self);
        root.addContent(getItems());
        XMLOutputter output = new XMLOutputter();
        output.setFormat(Format.getPrettyFormat());
        try {
            StringWriter res = new StringWriter();
            output.output(root, res);
            return res.toString();
        } catch (IOException e) {
            return null;
        }
    }
