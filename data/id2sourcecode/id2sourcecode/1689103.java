    public static Reader getReader(String source) throws WrappingRuntimeException {
        Reader r = null;
        boolean init = false;
        try {
            if (source.trim().toLowerCase().startsWith("http")) {
                URL url = new URL(source);
                r = new InputStreamReader(url.openStream());
                init = true;
            }
            if (!init) {
                r = new FileReader(new File(source));
            }
        } catch (Exception e) {
            throw new WrappingRuntimeException(e);
        }
        return r;
    }
