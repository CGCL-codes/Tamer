    public static Document createDocument(Content c) {
        Document doc = new Document();
        doc.add(new Field(ID, c.getId().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(SITE_ID, c.getSite().getId().toString(), Field.Store.NO, Field.Index.NOT_ANALYZED));
        doc.add(new Field(RELEASE_DATE, DateTools.dateToString(c.getReleaseDate(), Resolution.DAY), Field.Store.NO, Field.Index.NOT_ANALYZED));
        Channel channel = c.getChannel();
        while (channel != null) {
            doc.add(new Field(CHANNEL_ID_ARRAY, channel.getId().toString(), Field.Store.NO, Field.Index.NOT_ANALYZED));
            channel = channel.getParent();
        }
        doc.add(new Field(TITLE, c.getTitle(), Field.Store.NO, Field.Index.ANALYZED));
        if (!StringUtils.isBlank(c.getTxt())) {
            doc.add(new Field(CONTENT, c.getTxt(), Field.Store.NO, Field.Index.ANALYZED));
        }
        return doc;
    }
