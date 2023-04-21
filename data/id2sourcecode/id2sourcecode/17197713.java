    public void setURL(String url) throws ParserException {
        if ((null != url) && !"".equals(url)) setConnection(getConnectionManager().openConnection(url));
    }
