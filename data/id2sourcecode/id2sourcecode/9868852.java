    public static String getHost(final String link) {
        String host = cutLinkToHost(link);
        if (isNotNullOrEmpty(host)) {
            host = host.toLowerCase();
            while (host.startsWith(WWW_PREFIX)) {
                host = host.substring(WWW_PREFIX.length());
            }
            if (host.endsWith(".")) {
                host = host.substring(0, host.length() - 1);
            }
            if (host.indexOf(".") == 1) {
                return "";
            }
            try {
                final URL url = new URL(HTTP_PREFIX + host);
                url.openConnection();
                return url.getHost();
            } catch (final Exception e) {
                return "";
            }
        }
        return host;
    }
