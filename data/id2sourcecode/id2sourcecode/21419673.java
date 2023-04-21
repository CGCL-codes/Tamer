    protected Reader getLocalReader(Reader reader, boolean embedJavaInc) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Writer w = new OutputStreamWriter(out);
        try {
            Reader localReader = null;
            char[] buf = new char[Util.BUF_SIZE];
            int c;
            String stdHeader = embedJavaInc ? null : ((IContext) getContext()).getRedirectURL("/JavaBridge");
            localReader = new StringReader(getStandardHeader(stdHeader));
            while ((c = localReader.read(buf)) > 0) w.write(buf, 0, c);
            localReader.close();
            localReader = null;
            while ((c = reader.read(buf)) > 0) w.write(buf, 0, c);
            w.close();
            w = null;
            localReader = new InputStreamReader(new ByteArrayInputStream(out.toByteArray()));
            return localReader;
        } finally {
            if (w != null) try {
                w.close();
            } catch (IOException e) {
            }
        }
    }
