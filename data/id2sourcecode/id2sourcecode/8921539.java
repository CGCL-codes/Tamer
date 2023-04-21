    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Expires", "-1");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-control", "no-cache");
        response.setHeader("Content-Type", "text/xml; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        out.write("<data>");
        out.print("<items>");
        try {
            List news = NewsController.getRssItems(getAuthProfile(request));
            String channelTitle = "";
            String channelDesc = "";
            String channelUrl = "";
            if (news != null) {
                NewsItem item = null;
                for (int i = 0; i < news.size(); i++) {
                    item = (NewsItem) news.get(i);
                    out.print("<item>");
                    out.print("<date> " + Utility.htmlSpecialChars(item.getDate()) + "</date>");
                    out.print("<description> " + Utility.htmlSpecialChars(item.getDescription()) + "</description>");
                    out.print("<url> " + Utility.htmlSpecialChars(item.getLink()) + "</url>");
                    out.print("<title> " + Utility.htmlSpecialChars(item.getTitle()) + "</title>");
                    if (channelTitle == null || channelTitle.equals("")) {
                        channelTitle = Utility.htmlSpecialChars(item.getChannelTitle());
                    }
                    if (channelDesc == null || channelDesc.equals("")) {
                        channelDesc = Utility.htmlSpecialChars(item.getChannelDescription());
                    }
                    if (channelUrl == null || channelUrl.equals("")) {
                        channelUrl = Utility.htmlSpecialChars(item.getChannelUrl());
                    }
                    out.print("</item>");
                }
            }
            out.print("</items>");
            out.print("<channelTitle> " + channelTitle + "</channelTitle>");
            out.print("<channelDesc> " + channelDesc + "</channelDesc>");
            out.print("<channelUrl> " + channelUrl + "</channelUrl>");
            out.print("<result>0</result>");
        } catch (Exception e) {
            out.print("</items>");
            out.print("<result>1</result>");
        }
        out.print("</data>");
    }
