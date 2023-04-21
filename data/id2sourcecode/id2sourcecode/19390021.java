    private Object invokeUrlXml2(final Method method, final Object[] args) throws Throwable {
        final HttpURLConnection con = (HttpURLConnection) this.url.openConnection();
        con.setDoOutput(true);
        this.createHeader2(method, con);
        final XMLEncoder encoder = new XMLEncoder(con.getOutputStream());
        encoder.writeObject(args);
        Object result = null;
        if (con.getContentLength() > 0) {
            final XMLDecoder decoder = new XMLDecoder(con.getInputStream());
            result = decoder.readObject();
            if (result instanceof Throwable) {
                throw ((Throwable) result);
            }
        }
        con.disconnect();
        return result;
    }
