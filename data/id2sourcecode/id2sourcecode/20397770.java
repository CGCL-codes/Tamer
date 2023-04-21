    private void setFinalBuffer(ChannelBuffer buffer) throws ErrorDataDecoderException, IOException {
        currentAttribute.addContent(buffer, true);
        String value = decodeAttribute(currentAttribute.getChannelBuffer().toString(charset), charset);
        currentAttribute.setValue(value);
        addHttpData(currentAttribute);
        currentAttribute = null;
    }
