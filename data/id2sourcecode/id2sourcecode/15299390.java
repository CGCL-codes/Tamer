    public static URLConnectionEx openConnection(URL url) throws IOException {
        boolean isCompressionActive = false;
        if (url != null) {
            String protocol = url.getProtocol();
            if ("http".equals(protocol) || "https".equals(protocol)) {
                isCompressionActive = true;
            }
        }
        return new URLConnectionEx(url.openConnection(), isCompressionActive);
    }
