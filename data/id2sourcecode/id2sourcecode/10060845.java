    public void openItem() throws DocumentHandlerException {
        super.openItem();
        if (!isValid()) {
            return;
        }
        name = getStringAttribute(CommonAttributes.FONT_NAME, false, null);
        color = getColorAttribute(CommonAttributes.FONT_COLOR, true, CommonStyleAttributes.FONT_COLOR);
        String familyAttribute = getStringAttribute(CommonAttributes.FONT_FAMILY, true, CommonStyleAttributes.FONT_FAMILY);
        if (familyAttribute != null) {
            family = familyAttribute;
        }
        String encodingAttribute = getStringAttribute(CommonAttributes.FONT_ENCODING, true, CommonStyleAttributes.FONT_FAMILY);
        if (encodingAttribute != null) {
            encoding = encodingAttribute;
        }
        if (isAttributeDefined(CommonAttributes.FONT_SIZE, CommonStyleAttributes.FONT_SIZE)) {
            size = getDimensionAttribute(CommonAttributes.FONT_SIZE, true, CommonStyleAttributes.FONT_SIZE);
        }
        String styleAttribute = getStringAttribute(CommonAttributes.FONT_STYLE, true, CommonStyleAttributes.FONT_STYLE);
        if (styleAttribute != null) {
            style = Font.getStyleValue(styleAttribute);
        } else {
            styleAttribute = getStringAttribute(CommonAttributes.STYLE, true, null);
            if (styleAttribute != null) {
                style = Font.getStyleValue(styleAttribute);
            }
        }
        if (isAttributeDefined(CommonAttributes.TEXT_RISE, CommonStyleAttributes.TEXT_RISE)) {
            textRise = getDimensionAttribute(CommonAttributes.TEXT_RISE, true, CommonStyleAttributes.TEXT_RISE);
        }
        font = createFont(family, encoding, size, style, textRise, color);
        documentHandler.registerFont(name, font);
    }
