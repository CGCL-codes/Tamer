    @Override
    public InputStream openMIB(String mibName) throws IOException {
        final URL url = lookupMIB(mibName);
        if (url == null) throw new FileNotFoundException("Cannot resolve MIB: " + mibName);
        return url.openStream();
    }
