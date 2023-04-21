    @SuppressWarnings("unchecked")
    public Channel readChannel(ValidationEventHandler validationEventHandler) throws YarfrawException {
        Unmarshaller u;
        try {
            u = getUnMarshaller();
            u.setEventHandler(validationEventHandler);
            JAXBElement<TRss> o = (JAXBElement<TRss>) u.unmarshal(_file);
            TRss rss = o.getValue();
            TRssChannel channel = rss.getChannel();
            return ChannelMapperImpl.getInstance().execute(channel);
        } catch (JAXBException e) {
            throw new YarfrawException("Unable to unmarshal file", e);
        }
    }
