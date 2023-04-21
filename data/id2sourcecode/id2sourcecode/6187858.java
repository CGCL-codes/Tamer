    private RssChannelBean getChannelAtom() throws Exception {
        RssChannelBean res = new RssChannelBean();
        String pubDate;
        try {
            res.setTitle(doc.xpathSelectString("/feed/title/text()"));
            res.setDescription(doc.xpathSelectString("/feed/tagline/text()"));
            res.setLink(doc.xpathSelectString("/feed/link/@href"));
            pubDate = doc.xpathSelectString("/feed/modified/text()");
            if (pubDate != null) res.setPubDate(this.getDate(pubDate, TYPE_ATOM));
        } catch (Exception e) {
            throw new Exception("Error obteniendo elemento canal de " + filename, e);
        }
        return res;
    }
