    @Override
    public Script createContent() throws IOException {
        Context cx = Context.enter();
        Reader reader = null;
        try {
            URL url = getURL();
            reader = new InputStreamReader(url.openStream());
            return cx.compileReader(reader, url.toExternalForm(), 1, null);
        } finally {
            if (reader != null) {
                reader.close();
            }
            Context.exit();
        }
    }
