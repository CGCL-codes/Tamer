    public static void resetHead() {
        head = "<html><head><style type=\"text/css\">";
        head += " body	{font-family: " + GlobalSettings.guiFontFamily + ", verdana, courier, sans-serif; font-size: " + GlobalSettings.guiFontSize + "px;}";
        head += " .timestamp	{color: #" + makeColor(cs.getForegroundColor()) + ";}";
        head += " .channel	{color: #" + makeColor(cs.getChannelColor()) + ";}";
        head += " .info	{color: #" + makeColor(cs.getInfoColor()) + ";}";
        head += " .error	{color: #" + makeColor(cs.getErrorColor()) + ";}";
        head += " .debug	{font-family: courier; color: #" + makeColor(cs.getDebugColor()) + ";}";
        head += "</style></head><body>";
    }
