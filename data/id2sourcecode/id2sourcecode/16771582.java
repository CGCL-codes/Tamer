    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        if (!isWidgetSupported(protocol)) {
            return;
        }
        protocol.writeCloseSpan(spanAttributes);
        ChannelsCountAttributes channelsCountAttributes = (ChannelsCountAttributes) attributes;
        StringWriter scriptWriter = new StringWriter();
        scriptWriter.write("Ticker.createChannelsCount({");
        scriptWriter.write("id:" + createJavaScriptString(spanAttributes.getId()));
        if (channelsCountAttributes.getRead() != null) {
            scriptWriter.write(",read:" + (channelsCountAttributes.getRead().equals("no") ? "false" : "true"));
        }
        if (channelsCountAttributes.getFollowed() != null) {
            scriptWriter.write(",followed:" + (channelsCountAttributes.getFollowed().equals("no") ? "false" : "true"));
        }
        scriptWriter.write("})");
        addUsedFeedPollerId(protocol);
        writeJavaScript(scriptWriter.toString());
    }
