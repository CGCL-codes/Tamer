    public static long getLastModified(URL url) throws IOException {
        if (FILE_SCHEME.equalsIgnoreCase(url.getProtocol())) {
            try {
                return new File(url.toURI()).lastModified();
            } catch (URISyntaxException e) {
                throw new IOException(e);
            }
        } else {
            URLConnection uc = url.openConnection();
            uc.setDoInput(false);
            return uc.getLastModified();
        }
    }
