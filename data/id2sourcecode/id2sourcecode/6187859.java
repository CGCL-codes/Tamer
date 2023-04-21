    private RssChannelBean getChannelRss() throws Exception {
        RssChannelBean res = new RssChannelBean();
        String datePublish;
        try {
            if (this.rssType == TYPE_RDF) {
                res.setTitle(doc.xpathSelectString(this.xPath + "/channel/title/text()"));
                res.setLink(doc.xpathSelectString(this.xPath + "/channel/link/text()"));
                res.setDescription(doc.xpathSelectString(this.xPath + "/channel/description/text()"));
                datePublish = doc.xpathSelectString(this.xPath + "/channel/dc:date/text()");
            } else {
                res.setTitle(doc.xpathSelectString(this.xPath + "/title/text()"));
                res.setLink(doc.xpathSelectString(this.xPath + "/link/text()"));
                res.setDescription(doc.xpathSelectString(this.xPath + "/description/text()"));
                datePublish = doc.xpathSelectString(this.xPath + "/pubDate/text()");
            }
            if (datePublish != null) res.setPubDate(this.getDate(datePublish, this.rssType));
        } catch (Exception e) {
            throw new Exception("Error reading element channel from " + filename, e);
        }
        return res;
    }
