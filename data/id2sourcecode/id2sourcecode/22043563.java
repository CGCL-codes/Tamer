    private synchronized void initProperties() {
        properties = new Properties();
        int scan = getCommentCount();
        String vendor = getVendor();
        if (vendor != null) {
            properties.setProperty("vendor", vendor);
        }
        properties.setProperty("rate", String.valueOf(getRate()));
        properties.setProperty("channels", String.valueOf(getChannelCount()));
        while (--scan >= 0) {
            String comment = getComment(scan);
            int eq = comment == null ? -1 : comment.indexOf('=');
            if (eq >= 0) {
                properties.setProperty(comment.substring(0, eq).toLowerCase(), comment.substring(eq + 1));
            }
        }
        notifyAll();
    }
