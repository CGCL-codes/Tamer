    public void write(final Reader reader, final String possumDir, final Hashtable homologous, final Session s) throws IOException {
        final Writer2Ondex w = new Writer2Ondex(aog, s, reader, possumDir, homologous);
        w.start();
    }
