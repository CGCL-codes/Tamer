    public static final String getChannel(Element xmlConfig, int type) throws KETLThreadException {
        Node[] ports;
        if (type == ETLWorker.DEFAULT) ports = XMLHelper.getElementsByName(xmlConfig, "IN", "*", "*"); else ports = XMLHelper.getElementsByName(xmlConfig, "IN", type == ETLWorker.LEFT ? "LEFT" : "RIGHT", "TRUE");
        for (Node port : ports) {
            String content = XMLHelper.getTextContent(port);
            if (content == null) continue;
            content = content.trim();
            if (content.startsWith("\"") && content.endsWith("\"")) continue;
            String[] sources = content.split("\\.");
            if (sources == null || sources.length == 1 || sources.length > 3) throw new KETLThreadException("IN port definition invalid: \"" + content + "\"", Thread.currentThread());
            if (sources.length == 2) return "DEFAULT";
            return sources[1];
        }
        throw new KETLThreadException("Step \"" + XMLHelper.getAttributeAsString(xmlConfig.getAttributes(), "NAME", "n/a") + "\" has no in ports or ports do not have a valid source", Thread.currentThread());
    }
