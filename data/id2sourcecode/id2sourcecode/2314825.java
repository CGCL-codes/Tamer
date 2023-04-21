    public static void init() {
        if (init_) return;
        init_ = true;
        URLStreamHandler h = new URLStreamHandler() {

            protected URLConnection openConnection(URL _url) throws IOException {
                System.err.println("FFR: openConnection(" + _url + ")");
                return new Connection(_url);
            }
        };
        System.err.println("FFR: set the ram protocol for URLs");
        try {
            Field field = URL.class.getDeclaredField("handlers");
            setAccessible(field, true);
            Hashtable t = (Hashtable) field.get(null);
            t.put("ram", h);
        } catch (Exception ex) {
            System.err.println(ex);
        }
        try {
            Method m = File.class.getMethod("listRoots", new Class[0]);
            File[] l = (File[]) m.invoke(null, new Object[0]);
            for (int i = 0; i < l.length; i++) {
                String p = l[i].getPath();
                createRootPath(p);
            }
        } catch (NoSuchMethodException ex1) {
        } catch (Exception ex2) {
            ex2.printStackTrace();
        }
        createPropertyPath("user.dir");
        createPropertyPath("user.home");
        createPropertyPath("java.io.tmpdir");
    }
