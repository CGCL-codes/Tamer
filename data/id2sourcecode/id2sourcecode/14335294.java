    protected Sink getSink() {
        return new AbstractSink() {

            protected void doSink(final Tree tree) {
                try {
                    URLConnection cxn = url.openConnection();
                    cxn.setDoOutput(true);
                    PrintWriter w = new PrintWriter(cxn.getOutputStream());
                    getFormat().write(tree, w);
                    w.close();
                } catch (IOException e) {
                    throw new CongaRuntimeException(url.toString(), e);
                }
            }
        };
    }
