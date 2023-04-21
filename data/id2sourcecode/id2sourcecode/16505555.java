    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        if (!isWidgetSupported(protocol)) {
            return;
        }
        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);
        closeDivElement(protocol);
        TickerTapeAttributes tickerTapeAttributes = (TickerTapeAttributes) attributes;
        if (tickerTapeAttributes.getRefreshAttributes() != null) {
            require(WidgetScriptModules.BASE_AJAX, protocol, attributes);
        }
        FeedAttributes feedAttributes = tickerTapeAttributes.getFeedAttributes();
        String separatorId = null;
        if (feedAttributes != null) {
            Styles separatorStyles = feedAttributes.getStyles();
            if (separatorStyles != null) {
                separatorStyles = separatorStyles.getNestedStyles(PseudoElements.MCS_ITEM);
                if (separatorStyles != null) {
                    separatorStyles = separatorStyles.getNestedStyles(PseudoElements.MCS_BETWEEN);
                    if (separatorStyles != null) {
                        Element placeholderSpan = getCurrentBuffer(protocol).openStyledElement("span", StylingFactory.getDefaultInstance().createInheritedStyles(protocol.getMarinerPageContext().getStylingEngine().getStyles(), DisplayKeywords.NONE));
                        separatorId = protocol.getMarinerPageContext().generateUniqueFCID();
                        placeholderSpan.setAttribute("id", separatorId);
                        Element contentSpan = getCurrentBuffer(protocol).openStyledElement("span", separatorStyles);
                        StyleValue separatorContent = separatorStyles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.CONTENT);
                        if (separatorContent != null) {
                            ((DOMProtocol) protocol).getInserter().insert(contentSpan, separatorContent);
                        }
                        getCurrentBuffer(protocol).closeElement("span");
                        getCurrentBuffer(protocol).closeElement("span");
                    }
                }
            }
        }
        RefreshAttributes refreshAttributes = ((TickerTapeAttributes) attributes).getRefreshAttributes();
        StylesExtractor styles = createStylesExtractor(protocol, attributes.getStyles());
        StringWriter scriptWriter = new StringWriter();
        scriptWriter.write(createJavaScriptWidgetRegistrationOpening(attributes.getId()));
        addCreatedWidgetId(attributes.getId());
        scriptWriter.write("new Widget.TickerTape(" + createJavaScriptString(attributes.getId()) + ", {");
        scriptWriter.write("style:" + createJavaScriptString(styles.getMarqueeStyle()) + ",");
        scriptWriter.write("focusable:" + createJavaScriptString(styles.getFocusStyle()) + ",");
        scriptWriter.write("scroll:{");
        scriptWriter.write("direction:" + createJavaScriptString(styles.getMarqueeDirection()) + ",");
        scriptWriter.write("framesPerSecond:" + styles.getFrameRate() + ",");
        scriptWriter.write("charsPerSecond:" + styles.getMarqueeSpeed());
        scriptWriter.write("}");
        if (refreshAttributes != null) {
            scriptWriter.write(",refresh:{");
            scriptWriter.write("url:" + createJavaScriptString(refreshAttributes.getSrc()) + ",");
            scriptWriter.write("interval:" + createJavaScriptString(refreshAttributes.getInterval()));
            scriptWriter.write("}");
        }
        int repetitions = styles.getMarqueeRepetitions();
        scriptWriter.write(",repetitions:" + ((repetitions != Integer.MAX_VALUE) ? Integer.toString(repetitions) : createJavaScriptString("infinite")));
        scriptWriter.write("})");
        scriptWriter.write(createJavaScriptWidgetRegistrationClosure());
        scriptWriter.write(";");
        if (itemTemplateOutputBuffer != null) {
            getCurrentBuffer(protocol).transferContentsFrom(itemTemplateOutputBuffer);
            itemTemplateOutputBuffer = null;
        }
        if (feedAttributes != null) {
            require(ElementDefaultRenderer.WIDGET_TICKER, protocol, attributes);
            scriptWriter.write("Ticker.createTickerTapeController({tickerTape:Widget.getInstance(" + createJavaScriptString(attributes.getId()) + ")");
            if (feedAttributes.getChannel() != null) {
                scriptWriter.write(", channel:" + createJavaScriptString(feedAttributes.getChannel()));
            }
            if (feedAttributes.getItemDisplay() != null) {
                scriptWriter.write(", itemDisplayId:" + createJavaScriptString(feedAttributes.getItemDisplay()));
            }
            if (separatorId != null) {
                scriptWriter.write(", separatorId:" + createJavaScriptString(separatorId));
            }
            if (itemTemplateId != null) {
                scriptWriter.write(", itemTemplate:" + createJavaScriptWidgetReference(itemTemplateId));
                addUsedWidgetId(itemTemplateId);
            }
            scriptWriter.write("});");
            addUsedWidgetId(protocol.getMarinerPageContext().generateFCID(ElementDefaultRenderer.FEED_POLLER_ID_SUFFIX));
        }
        writeJavaScript(scriptWriter.toString());
    }
