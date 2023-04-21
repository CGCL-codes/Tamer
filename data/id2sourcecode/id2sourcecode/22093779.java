    String getMIMEtype(URL url) throws IOException {
        return url.openConnection().getContentType();
    }
