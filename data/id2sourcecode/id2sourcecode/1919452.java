    protected void init(Hashtable environment) throws NamingException {
        final String[] properties = { Context.DNS_URL, Context.INITIAL_CONTEXT_FACTORY, Context.OBJECT_FACTORIES, Context.PROVIDER_URL, Context.STATE_FACTORIES, Context.URL_PKG_PREFIXES };
        if (environment != null) myProps = (Hashtable) environment.clone(); else myProps = new Hashtable();
        Applet napplet = (Applet) myProps.get(Context.APPLET);
        for (int i = properties.length - 1; i >= 0; i--) {
            Object o = myProps.get(properties[i]);
            if (o == null) {
                if (napplet != null) o = napplet.getParameter(properties[i]);
                if (o == null) o = System.getProperty(properties[i]);
                if (o != null) myProps.put(properties[i], o);
            }
        }
        try {
            Enumeration ep = Thread.currentThread().getContextClassLoader().getResources("jndi.naming");
            while (ep.hasMoreElements()) {
                URL url = (URL) ep.nextElement();
                Properties p = new Properties();
                try {
                    InputStream is = url.openStream();
                    p.load(is);
                    is.close();
                } catch (IOException e) {
                }
                merge(myProps, p);
            }
        } catch (IOException e) {
        }
        String home = System.getProperty("gnu.classpath.home.url");
        if (home != null) {
            String url = home + "/jndi.properties";
            Properties p = new Properties();
            try {
                InputStream is = new URL(url).openStream();
                p.load(is);
                is.close();
            } catch (IOException e) {
            }
            merge(myProps, p);
        }
    }
