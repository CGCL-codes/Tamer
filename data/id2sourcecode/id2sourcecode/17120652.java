    public static Object getItem(String url) throws RemoteException, NotBoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, MalformedURLException {
        Object item = null;
        if (url == null) url = "///main"; else if (url.startsWith("//") && url.endsWith("/")) url += "main";
        if (url.startsWith("//")) {
            item = java.rmi.Naming.lookup(url);
        } else if (url.startsWith("/")) {
            InputStream ris = Remote.class.getResourceAsStream(url);
            if (ris == null) ris = new FileInputStream('.' + url);
            item = zedmob(ris);
            ris.close();
        } else if (url.indexOf(':') == -1) {
            item = Class.forName(url).newInstance();
        } else {
            InputStream uis = new URL(url).openStream();
            item = zedmob(uis);
            uis.close();
        }
        return item;
    }
