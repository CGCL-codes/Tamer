    public InputStream getResourceAsStream(String name) {
        final URL url = getResource(name);
        if (url == null) {
            return null;
        }
        try {
            return (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction() {

                public Object run() throws IOException {
                    return url.openStream();
                }
            }, acc);
        } catch (PrivilegedActionException e) {
            if (debug) {
                debug("Unable to find resource for class " + name + " " + e.toString());
            }
            return null;
        }
    }
