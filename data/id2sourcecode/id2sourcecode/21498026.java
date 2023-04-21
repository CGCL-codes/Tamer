    public static OutputStream openOutputStreamForURI(String uri) throws IOException {
        if (uri.startsWith("file:")) {
            uri = uri.substring(5);
            if (uri.startsWith("//")) uri = uri.substring(2);
            return new FileOutputStream(uri);
        } else if (uri.contains("://")) {
            try {
                URL url = new URL(uri);
                URLConnection urlc = url.openConnection();
                return urlc.getOutputStream();
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return new FileOutputStream(uri);
    }
