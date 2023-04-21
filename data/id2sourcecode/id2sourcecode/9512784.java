    public static <T> Iterator<T> providers(Class<T> c) {
        ArrayList<T> objects = new ArrayList<T>();
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        String cn = c.getName();
        Enumeration<URL> urls = null;
        try {
            urls = cl.getResources("META-INF/services/" + cn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (urls == null) return objects.iterator();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            try {
                InputStream is = url.openStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                String className = null;
                while ((className = br.readLine()) != null) {
                    if (className.contains("#")) {
                        className = className.substring(0, className.indexOf('#'));
                    }
                    className = className.trim();
                    if (className.equals("")) continue;
                    try {
                        Class<?> theClass = cl.loadClass(className);
                        Class<? extends T> tClass = theClass.asSubclass(c);
                        objects.add(tClass.newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return objects.iterator();
    }
