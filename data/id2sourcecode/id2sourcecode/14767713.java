    public void addVerticalPadding(Element element, String attribute) {
        int topPadding = getPixelLength(styles.getStyleValue(StylePropertyDetails.PADDING_TOP));
        int bottomPadding = getPixelLength(styles.getStyleValue(StylePropertyDetails.PADDING_BOTTOM));
        int vpad = (topPadding + bottomPadding) / 2;
        if (vpad != 0) {
            element.setAttribute(attribute, String.valueOf(vpad));
        }
    }
