    public static final String[] getChannels(Element config) {
        NodeList nl = config.getElementsByTagName("OUT");
        HashSet ports = new HashSet();
        for (int i = 0; i < nl.getLength(); i++) {
            ports.add(XMLHelper.getAttributeAsString(nl.item(i).getAttributes(), "CHANNEL", "DEFAULT"));
        }
        String[] res = new String[ports.size()];
        ports.toArray(res);
        return res;
    }
