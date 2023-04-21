    public static final void main(String args[]) {
        try {
            final HttpClient c = new HttpClient(args[0]).connect();
            final InputStream i = c.getResponseStream();
            for (int b = i.read(); b >= 0; b = i.read()) System.out.write(b);
            c.disconnect();
        } catch (Throwable throwable) {
            throwable.printStackTrace(System.err);
        }
    }
