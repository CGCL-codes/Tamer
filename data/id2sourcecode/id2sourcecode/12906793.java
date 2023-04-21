    protected boolean isHtml(String link) throws ParserException {
        URL url;
        URLConnection connection;
        String type;
        boolean ret;
        ret = false;
        try {
            url = new URL(link);
            connection = url.openConnection();
            type = connection.getContentType();
            if (type == null) ret = false; else ret = type.startsWith("text/html");
        } catch (Exception e) {
            throw new ParserException("URL " + link + " has a problem", e);
        }
        return (ret);
    }
