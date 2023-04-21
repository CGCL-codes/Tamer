    public int Flush(PlugInChannelMap ch, boolean doStream) throws SAPIException {
        if (doStream) throw new IllegalArgumentException("Streaming not supported for PlugIns.");
        int toFlush = ch.getChannelsPut();
        try {
            Rmap response = ch.produceOutput(doStream);
            plugin.addChild(response);
            ch.clearData();
            ch.incrementNext();
        } catch (Exception e) {
            throw new SAPIException(e);
        }
        return toFlush;
    }
