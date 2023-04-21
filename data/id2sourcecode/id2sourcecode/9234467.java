    public static InputStream open(String name) throws FileNotFoundException, IOException {
        InputStream is = null;
        if (name.indexOf(':') != -1) {
            try {
                URL url = new URL(name);
                is = url.openStream();
            } catch (Exception e) {
            }
        }
        if (is == null) {
            is = new FileInputStream(name);
        }
        if (name.endsWith(".gz") || name.endsWith(".Z")) {
            is = new GZIPInputStream(is);
        }
        if (is != null && !(is instanceof BufferedInputStream)) {
            is = new BufferedInputStream(is);
        }
        return is;
    }
