    @Override
    public String getString() {
        WikiEngine engine = m_wikiContext.getEngine();
        Element root = new Element("rss");
        root.setAttribute("version", "2.0");
        Element channel = new Element("channel");
        root.addContent(channel);
        channel.addContent(new Element("title").setText(getChannelTitle()));
        channel.addContent(new Element("link").setText(engine.getBaseURL()));
        channel.addContent(new Element("description").setText(getChannelDescription()));
        channel.addContent(new Element("language").setText(getChannelLanguage()));
        channel.addContent(new Element("generator").setText("JSPWiki " + Release.VERSTR));
        String mail = engine.getVariable(m_wikiContext, RSSGenerator.PROP_RSS_AUTHOREMAIL);
        if (mail != null) {
            String editor = engine.getVariable(m_wikiContext, RSSGenerator.PROP_RSS_AUTHOR);
            if (editor != null) mail = mail + " (" + editor + ")";
            channel.addContent(new Element("managingEditor").setText(mail));
        }
        channel.addContent(getItems());
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
