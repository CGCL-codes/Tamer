    public void addHorizontalSpace(Element element, String attribute) {
        int leftMargin = getPixelLength(styles.getStyleValue(StylePropertyDetails.MARGIN_LEFT));
        int rightMargin = getPixelLength(styles.getStyleValue(StylePropertyDetails.MARGIN_RIGHT));
        int hspace = (leftMargin + rightMargin) / 2;
        if (hspace != 0) {
            element.setAttribute(attribute, String.valueOf(hspace));
        }
    }
