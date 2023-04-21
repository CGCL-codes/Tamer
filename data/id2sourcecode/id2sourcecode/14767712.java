    public void addHorizontalPadding(Element element, String attribute) {
        int leftPadding = getPixelLength(styles.getStyleValue(StylePropertyDetails.PADDING_LEFT));
        int rightPadding = getPixelLength(styles.getStyleValue(StylePropertyDetails.PADDING_RIGHT));
        int hpad = (leftPadding + rightPadding) / 2;
        if (hpad != 0) {
            element.setAttribute(attribute, String.valueOf(hpad));
        }
    }
