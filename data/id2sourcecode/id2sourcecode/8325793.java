    @Override
    public void write(OutputStream outs) throws IOException {
        InputStream ins = url.openStream();
        try {
            ContentUtil.pipe(ins, outs);
        } finally {
            try {
                ins.close();
            } catch (Exception ex) {
                log.log(Level.WARNING, "unable to close " + url, ex);
            }
        }
    }
