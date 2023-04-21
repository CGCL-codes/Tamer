    public void addVerticalSpace(Element element, String attribute) {
        int topMargin = getPixelLength(styles.getStyleValue(StylePropertyDetails.MARGIN_TOP));
        int bottomMargin = getPixelLength(styles.getStyleValue(StylePropertyDetails.MARGIN_BOTTOM));
        int vspace = (topMargin + bottomMargin) / 2;
        if (vspace != 0) {
            element.setAttribute(attribute, String.valueOf(vspace));
        }
    }
