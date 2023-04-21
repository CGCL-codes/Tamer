    public Rss20Appender addAllItems(List<Item> items) throws YarfrawException {
        Channel ch = readChannel();
        ch.getItems().addAll(items);
        trimItemsList(ch.getItems());
        _writer = new Rss20Writer(_reader._file);
        _writer.writeChannel(ch);
        return this;
    }
