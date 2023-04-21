    private static void openText(String docName) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);
            Object document = bundle.getObject(docName + ".sample");
            MConstText text;
            if (document instanceof String) {
                text = new StyledText((String) document, AttributeMap.EMPTY_ATTRIBUTE_MAP);
            } else {
                URL url = (URL) document;
                ObjectInputStream in = new ObjectInputStream(url.openStream());
                text = (MConstText) in.readObject();
            }
            String name = bundle.getString(docName + ".name");
            makeFrame(text, name);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
