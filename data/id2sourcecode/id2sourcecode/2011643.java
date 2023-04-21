    public URLConnection openConnection(URL u) throws IOException {
        String ustr = u.toString();
        String rstr = ustr.substring(CLASS_RESOURCE_URI_PREFIX_LENGTH);
        int sl = rstr.indexOf('/');
        if (sl > 0) rstr = rstr.substring(0, sl).replace('.', '/') + rstr.substring(sl);
        URL url = cloader.getResource(rstr);
        if (url == null) throw new FileNotFoundException(ustr);
        return url.openConnection();
    }
