    public static InputStream streamFromString(String location) throws IOException {
        InputStream is = null;
        URL url = urlFromString(location, null, false);
        if (url != null) {
            is = url.openStream();
        } else {
            File f = new File(location);
            if (f.exists()) is = new FileInputStream(f);
        }
        if (is == null) {
            return null;
        } else if (isGZipFile(location)) {
            return new GZIPInputStream(is);
        } else {
            return is;
        }
    }
