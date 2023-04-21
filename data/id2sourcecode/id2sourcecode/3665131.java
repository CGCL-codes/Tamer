    public static Document makeDocument(ItemIF item) {
        Document doc = new Document();
        doc.add(Field.Text(TITLE, item.getTitle()));
        doc.add(Field.Text(DESCRIPTION, item.getDescription()));
        doc.add(Field.Text(TITLE_AND_DESC, item.getTitle() + " " + item.getDescription()));
        if (item.getFound() != null) {
            doc.add(Field.Keyword(DATE_FOUND, DateField.dateToString(item.getFound())));
        }
        doc.add(Field.UnIndexed(ITEM_ID, Long.toString(item.getId())));
        if (item.getChannel() != null) {
            doc.add(Field.UnIndexed(CHANNEL_ID, Long.toString(item.getChannel().getId())));
        }
        return doc;
    }
