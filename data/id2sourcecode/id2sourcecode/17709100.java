    private void buildOpenSearchRss10() throws OpenSearchRssException {
        StringBuffer buff = new StringBuffer();
        OpenSearchRssChannel osrChannel = getChannel();
        if (osrChannel == null) return;
        validateSomeValues(osrChannel);
        buff.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + XMLUtil.RET);
        buff.append("<rss version=\"2.0\"  xmlns:openSearch=\"http://a9.com/-/spec/opensearchrss/1.0/\">" + RET);
        buff.append("	<channel>" + RET);
        buff.append(XMLUtil.buildXMLNodeText(8, "title", osrChannel.getTitle()));
        buff.append(XMLUtil.buildXMLNodeText(8, "link", osrChannel.getLink().toExternalForm()));
        buff.append(XMLUtil.buildXMLNodeText(8, "description", osrChannel.getDescription()));
        if (osrChannel.getLanguage() != null) buff.append(XMLUtil.buildXMLNodeText(8, "language", getLanguage(osrChannel.getLanguage())));
        if (osrChannel.getCopyright() != null) buff.append(XMLUtil.buildXMLNodeText(8, "copyright", osrChannel.getCopyright()));
        buff.append(XMLUtil.buildXMLNodeText(8, "openSearch:totalResults", osrChannel.getTotalResult()));
        buff.append(XMLUtil.buildXMLNodeText(8, "openSearch:startIndex", osrChannel.getStartIndex()));
        buff.append(XMLUtil.buildXMLNodeText(8, "openSearch:itemsPerPage", osrChannel.getItemsPerPage()));
        List items = osrChannel.items();
        Iterator it = items.iterator();
        while (it.hasNext()) {
            OpenSearchRssItem item = (OpenSearchRssItem) it.next();
            buff.append("		<item>" + RET);
            buff.append(XMLUtil.buildXMLNodeText(12, "title", item.getTitle()));
            buff.append(XMLUtil.buildXMLNodeText(12, "link", item.getLink().toExternalForm()));
            buff.append(XMLUtil.buildXMLNodeText(12, "description", item.getDescription()));
            buff.append("		</item>" + RET);
        }
        buff.append("	</channel>" + RET);
        buff.append("</rss>" + RET);
        m_result = new String(buff);
    }
