    protected static Properties getProperties(final URL url) {
        PrivilegedAction action = new PrivilegedAction() {

            public Object run() {
                Properties props = null;
                try {
                    InputStream stream;
                    stream = url.openStream();
                    props = new Properties();
                    props.load(stream);
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return props;
            }
        };
        return (Properties) AccessController.doPrivileged(action);
    }
