    @SuppressWarnings("unchecked")
    public ChannelFeed execute(RDF rdf) throws YarfrawException {
        if (rdf == null) {
            LOG.warn("null rdf Element received, this is not normal. ");
            return null;
        }
        TRss10Channel ch = null;
        TRss10TextInput ti = null;
        TRss10Image img = null;
        for (Object o : rdf.getChannelOrImageOrItem()) {
            if (o instanceof JAXBElement) {
                Object val = ((JAXBElement) o).getValue();
                if (val instanceof TRss10Channel) {
                    ch = (TRss10Channel) val;
                } else if (val instanceof TRss10TextInput) {
                    ti = (TRss10TextInput) val;
                } else if (val instanceof TRss10Image) {
                    img = (TRss10Image) val;
                }
            }
        }
        ChannelFeed channel = toChannel(ch, rdf);
        if (img != null) {
            if (channel.getImageOrIcon() != null) {
                Rss10MappingUtils.populateImage(channel.getImageOrIcon(), img);
            } else {
                LOG.warn("expecting an <image> element under <channel>");
                Image newImage = new Image();
                newImage.setResource(img.getAbout());
                Rss10MappingUtils.populateImage(newImage, img);
                channel.setImageOrIcon(newImage);
            }
        }
        if (ti != null) {
            if (channel.getTexInput() != null) {
                Rss10MappingUtils.populateTextinput(channel.getTexInput(), ti);
            } else {
                LOG.warn("expecting an <textinput> element under <channel>");
                TextInput newTextInput = new TextInput();
                newTextInput.setResource(ti.getAbout());
                Rss10MappingUtils.populateTextinput(newTextInput, ti);
                channel.setTexInput(newTextInput);
            }
        }
        return channel;
    }
