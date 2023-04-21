    private XML getChannelElement() {
        XML channel = new XML("channel");
        channel.addAttribute("rdf:about", m_feedURL);
        channel.addElement(new XML("link").addElement(m_feedURL));
        if (m_channelTitle != null) channel.addElement(new XML("title").addElement(format(m_channelTitle)));
        if (m_channelDescription != null) channel.addElement(new XML("description").addElement(format(m_channelDescription)));
        if (m_channelLanguage != null) channel.addElement(new XML("dc:language").addElement(m_channelLanguage));
        channel.setPrettyPrint(true);
        channel.addElement(getRDFItems());
        return channel;
    }
