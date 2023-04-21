    public RDF execute(ChannelFeed ch) throws YarfrawException {
        RDF rdf = FACTORY.createRDF();
        List<Object> elementList = rdf.getChannelOrImageOrItem();
        elementList.add(Rss10MappingUtils.toChannel(ch));
        if (ch.getImageOrIcon() != null) {
            elementList.add(Rss10MappingUtils.toRss10Image(ch.getImageOrIcon()));
        }
        if (ch.getItems() != null) {
            for (ItemEntry item : ch.getItems()) {
                if (item != null) {
                    elementList.add(ToRss10ChannelItemImpl.getInstance().execute(item));
                }
            }
        }
        if (ch.getTexInput() != null) {
            elementList.add(Rss10MappingUtils.toRss10TextInput(ch.getTexInput()));
        }
        return rdf;
    }
