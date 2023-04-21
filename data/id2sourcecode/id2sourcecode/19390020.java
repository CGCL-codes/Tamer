    private Object invokeUrlBinary2(final Method method, final Object[] args) throws Throwable {
        final HttpURLConnection con = (HttpURLConnection) this.url.openConnection();
        con.setDoOutput(true);
        this.createHeader2(method, con);
        final ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
        oos.writeObject(args);
        oos.close();
        Object result = null;
        if (con.getContentLength() > 0) {
            final ObjectInputStream ois = new ObjectInputStream(con.getInputStream());
            result = ois.readObject();
            if (result instanceof Throwable) {
                throw ((Throwable) result);
            }
        }
        con.disconnect();
        return result;
    }
