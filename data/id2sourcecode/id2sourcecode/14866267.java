    public InputStream getResourceAsStream(String name) {
        if (name.endsWith(".class")) {
            return null;
        }
        int i;
        while ((i = name.indexOf("/./")) >= 0) {
            name = name.substring(0, i) + name.substring(i + 2);
        }
        i = 0;
        int limit;
        while ((i = name.indexOf("/../", i)) > 0) {
            if ((limit = name.lastIndexOf('/', i - 1)) >= 0) {
                name = name.substring(0, limit) + name.substring(i + 3);
                i = 0;
            } else {
                i = i + 3;
            }
        }
        if (name.startsWith("/") || name.startsWith(File.separator)) {
            name = name.substring(1);
        }
        final String n = name;
        InputStream retval;
        retval = (InputStream) AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                URL url = findResource(n);
                try {
                    return url != null ? url.openStream() : null;
                } catch (IOException e) {
                    return null;
                }
            }
        }, ac);
        return retval;
    }
