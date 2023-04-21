    @Override
    public StreamSource[] resolve(String moduleURI, String baseURI, String[] locations) throws XPathException {
        if (moduleURI.equals("http://www.functx.com")) {
            URL url = mShell.getResource("/org/xmlsh/resources/modules/functx.xquery");
            if (url == null) return null;
            try {
                URI uri = url.toURI();
                StreamSource[] result = new StreamSource[1];
                result[0] = new StreamSource(url.openStream(), uri.toString());
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
