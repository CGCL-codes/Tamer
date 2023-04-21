    protected Map loadURL(String name) throws Exception {
        long start = System.currentTimeMillis();
        URL url = this.getClass().getResource(name);
        ObjectInputStream si = new ObjectInputStream(url.openStream());
        Map dict = (Map) si.readObject();
        long end = System.currentTimeMillis();
        si.close();
        return dict;
    }
