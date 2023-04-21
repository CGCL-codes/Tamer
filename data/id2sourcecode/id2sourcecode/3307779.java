    private Object getObject(URL url) throws Exception {
        Object objRet = null;
        System.out.println("requesting: " + url);
        URLConnection urlConn = url.openConnection();
        urlConn.setUseCaches(false);
        ObjectInputStream ois = new ObjectInputStream(urlConn.getInputStream());
        objRet = ois.readObject();
        ois.close();
        return objRet;
    }
