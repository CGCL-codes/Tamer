    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        if (!isWidgetSupported(protocol)) {
            return;
        }
        protocol.writeCloseSpan(spanAttributes);
        ItemsCountAttributes itemsCountAttributes = (ItemsCountAttributes) attributes;
        StringWriter scriptWriter = new StringWriter();
        scriptWriter.write("Ticker.createItemsCount({");
        scriptWriter.write("id:" + createJavaScriptString(spanAttributes.getId()));
        if (itemsCountAttributes.getChannel() != null) {
            scriptWriter.write(",channel:" + createJavaScriptString(itemsCountAttributes.getChannel()));
        }
        if (itemsCountAttributes.getRead() != null) {
            scriptWriter.write(",read:" + (itemsCountAttributes.getRead().equals("no") ? "false" : "true"));
        }
        if (itemsCountAttributes.getFollowed() != null) {
            scriptWriter.write(",followed:" + (itemsCountAttributes.getFollowed().equals("no") ? "false" : "true"));
        }
        scriptWriter.write("})");
        addUsedFeedPollerId(protocol);
        writeJavaScript(scriptWriter.toString());
    }
