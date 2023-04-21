    public static String getChannelAttributeValue(final Element element) {
        for (int i = 0; i < styles.length; i++) {
            String string = element.getAttribute(styles[i].getChannelAttributeName());
            if (!string.equals("")) {
                return string;
            }
        }
        return "";
    }
