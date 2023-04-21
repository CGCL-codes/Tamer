    public long getLastModified(Object src) {
        if (src instanceof URL) {
            final URL url = (URL) src;
            final String protocol = url.getProtocol().toLowerCase();
            if (!"http".equals(protocol) && !"https".equals(protocol) && !"ftp".equals(protocol)) {
                try {
                    return url.openConnection().getLastModified();
                } catch (IOException ex) {
                    return -1;
                }
            }
            return -1;
        } else if (src instanceof File) {
            return ((File) src).lastModified();
        } else if (src == null) {
            throw new NullPointerException();
        } else {
            throw new IllegalArgumentException("Unknown soruce: " + src + "\nOnly File and URL are supported");
        }
    }
