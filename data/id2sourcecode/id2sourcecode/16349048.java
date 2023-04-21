    public static Document createDocument(Article a) {
        Document doc = new Document();
        doc.add(new Field(ID, a.getId().toString(), Field.Store.YES, Field.Index.NO));
        doc.add(new Field(WEBSITE_ID, a.getWebsite().getId().toString(), Field.Store.NO, Field.Index.NOT_ANALYZED));
        doc.add(new Field(RELEASE_DATE, DateTools.dateToString(a.getReleaseDate(), Resolution.DAY), Field.Store.NO, Field.Index.NOT_ANALYZED));
        for (Long id : a.getChannelIds()) {
            doc.add(new Field(CHANNEL_ID_ARRAY, id.toString(), Field.Store.NO, Field.Index.NOT_ANALYZED));
        }
        doc.add(new Field(TITLE, a.getTitle(), Field.Store.NO, Field.Index.ANALYZED));
        doc.add(new Field(CONTENT, a.getContentFromFile(), Field.Store.NO, Field.Index.ANALYZED));
        return doc;
    }
