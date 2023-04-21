    public ModelAndView exportOPML(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List channels = channelController.getChannelsToPoll();
        StringBuffer sb = new StringBuffer();
        sb.append("&lt;opml version=\"1.1\"&gt;<br>\n" + "&nbsp;&nbsp;&nbsp;&lt;head&gt;<br>\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;title&gt;rssReader Export&lt;/title&gt;<br>\n" + "&nbsp;&nbsp;&nbsp;&lt;/head&gt;<br>\n" + "&lt;body&gt;<br>\n");
        for (int i = 0; i < channels.size(); i++) {
            Channel channel = (Channel) channels.get(i);
            sb.append("&nbsp;&nbsp;&nbsp;&lt;outline");
            sb.append(" text=\"").append(StringUtils.replaceForXML(channel.getTitle())).append("\"");
            sb.append(" desctiption=\"").append(StringUtils.replaceForXML(channel.getDescription())).append("\"");
            sb.append(" language=\"").append(StringUtils.replaceForXML(channel.getLanguage())).append("\"");
            sb.append(" title=\"").append(StringUtils.replaceForXML(channel.getTitle())).append("\"");
            sb.append(" xmlUrl=\"").append(StringUtils.replaceForXML(channel.getUrl())).append("\"");
            sb.append("/&gt;<br>");
        }
        sb.append("&lt;/body&gt;<br>&lt;/opml&gt;");
        return new ModelAndView("", "exportOPML", sb.toString());
    }
