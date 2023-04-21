    public static OutputStream openOutputStream(URL url, long lastModified) throws IOException {
        if (FILE_SCHEME.equalsIgnoreCase(url.getProtocol())) {
            try {
                return new PreservingFileOutputStream(new File(url.toURI()), lastModified);
            } catch (URISyntaxException e) {
                throw new IOException(e);
            }
        } else {
            URLConnection uc = url.openConnection();
            uc.setDoInput(false);
            uc.setDoOutput(true);
            uc.connect();
            return uc.getOutputStream();
        }
    }
