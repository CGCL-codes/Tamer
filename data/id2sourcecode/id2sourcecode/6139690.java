    public static LoggerFactory newLoggerFactory() {
        String p = LoggerFactory.class.getName();
        String v = System.getProperty(p);
        LoggerFactory result;
        if (v != null) {
            result = newLoggerFactory(v);
            if (result != null) {
                return result;
            }
        }
        String res = "META-INF/services/" + p;
        ClassLoader cl = LoggerAccess.class.getClassLoader();
        if (cl == null) {
            cl = ClassLoader.getSystemClassLoader();
        }
        URL url = cl.getResource(res);
        if (url == null) {
            cl = Thread.currentThread().getContextClassLoader();
            if (cl != null) {
                url = cl.getResource(res);
            }
        }
        if (url != null) {
            InputStream istream = null;
            try {
                istream = url.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
                v = reader.readLine();
                if (v != null) {
                    result = newLoggerFactory(v);
                }
                istream.close();
                istream = null;
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                if (istream != null) {
                    try {
                        istream.close();
                    } catch (Throwable ignore) {
                    }
                }
            }
        }
        return new LoggerFactoryImpl() {

            public Logger newLogger(String pName) {
                return new LoggerImpl(pName);
            }
        };
    }
