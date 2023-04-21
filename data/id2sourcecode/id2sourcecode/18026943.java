    private ChemSequence parseFeed(RSSChannel channel) throws CDKException, IOException {
        URL url = channel.getURL();
        logger.debug("Should load this RSS now: ", url);
        ChemSequence channelItems = null;
        InputStream is = url.openStream();
        InputStreamReader isReader = new InputStreamReader(is);
        ChemicalRSSReader reader = new ChemicalRSSReader(isReader);
        channelItems = (ChemSequence) reader.read(new ChemSequence());
        return channelItems;
    }
