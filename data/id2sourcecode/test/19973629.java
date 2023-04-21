    private static Object getObjectFromUrl(URL url) throws Exception {
        Object obj;
        InputStream is = url.openStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        obj = ois.readObject();
        ois.close();
        return obj;
    }
