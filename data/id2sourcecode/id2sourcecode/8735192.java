    @SuppressWarnings("unchecked")
    private static ChannelFeed toChannel(FeedFormat format, Object o) throws YarfrawException {
        if (format == FeedFormat.RSS20) {
            return ToChannelRss20Impl.getInstance().execute(((JAXBElement<TRss>) o).getValue().getChannel());
        } else if (format == FeedFormat.RSS10) {
            return ToChannelRss10Impl.getInstance().execute((RDF) o);
        } else if (format == FeedFormat.ATOM10) {
            return ToChannelAtom10Impl.getInstance().execute(((JAXBElement<FeedType>) o).getValue());
        } else if (format == FeedFormat.ATOM03) {
            return ToChannelAtom03Impl.getInstance().execute(((JAXBElement<yarfraw.generated.atom03.elements.FeedType>) o).getValue());
        } else {
            throw new UnsupportedOperationException("Unknown Feed Format");
        }
    }
