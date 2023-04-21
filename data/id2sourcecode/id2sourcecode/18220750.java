    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        if (!isWidgetSupported(protocol)) {
            return;
        }
        if (((CarouselAttributes) attributes).getRefreshAttributes() != null) {
            require(WidgetScriptModules.BASE_AJAX, protocol, attributes);
        }
        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);
        currentBuffer.closeElement("ul");
        closeDivElement(currentBuffer);
        closeDivElement(currentBuffer);
        RefreshAttributes refreshAttributes = ((CarouselAttributes) attributes).getRefreshAttributes();
        String textRefreshAttr = "";
        if (refreshAttributes != null) {
            textRefreshAttr = ", refreshURL: " + createJavaScriptString(refreshAttributes.getSrc()) + ", refreshInterval: " + refreshAttributes.getInterval();
        }
        StylesExtractor styles = createStylesExtractor(protocol, attributes.getStyles());
        StylesExtractor disappearStyles = createStylesExtractor(protocol, attributes.getStyles());
        disappearStyles.setPseudoClass(StatefulPseudoClasses.MCS_CONCEALED);
        StringBuffer textToScriptBuffer = new StringBuffer();
        textToScriptBuffer.append("Widget.register(").append(createJavaScriptString(attributes.getId())).append(",new Widget.Carousel(").append(createJavaScriptString(attributes.getId())).append(", {").append("delay: ").append(styles.getTransitionInterval()).append(", ").append(getAppearableOptions(styles)).append(", ").append(getDisappearableOptions(disappearStyles)).append(textRefreshAttr).append(", focusable: ").append(createJavaScriptString(styles.getFocusStyle())).append("}));");
        addCreatedWidgetId(attributes.getId());
        String textToScript = textToScriptBuffer.toString();
        if (itemTemplateOutputBuffer != null) {
            getCurrentBuffer(protocol).transferContentsFrom(itemTemplateOutputBuffer);
            itemTemplateOutputBuffer = null;
        }
        FeedAttributes feedAttributes = ((CarouselAttributes) attributes).getFeedAttributes();
        if (feedAttributes != null) {
            require(ElementDefaultRenderer.WIDGET_TICKER, protocol, attributes);
            StringWriter scriptWriter = new StringWriter();
            scriptWriter.write("Ticker.createCarouselController({");
            scriptWriter.write("carousel:Widget.getInstance(" + createJavaScriptString(attributes.getId()) + ")");
            if (feedAttributes.getChannel() != null) {
                scriptWriter.write(", channel:" + createJavaScriptString(feedAttributes.getChannel()));
            }
            if (feedAttributes.getItemDisplay() != null) {
                scriptWriter.write(", itemDisplayId:" + createJavaScriptString(feedAttributes.getItemDisplay()));
            }
            if (itemTemplateId != null) {
                scriptWriter.write(", itemTemplate:" + createJavaScriptWidgetReference(itemTemplateId));
                addUsedWidgetId(itemTemplateId);
                itemTemplateId = null;
            }
            scriptWriter.write("});");
            textToScript = textToScript + scriptWriter.toString();
            addUsedWidgetId(protocol.getMarinerPageContext().generateFCID(ElementDefaultRenderer.FEED_POLLER_ID_SUFFIX));
        }
        writeJavaScript(textToScript);
    }
