    private void readFileForWithHashMapRules1() throws DException {
        URL url1 = null;
        if (url == null) {
            url1 = getClass().getResource("/com/daffodilwoods/daffodildb/utils/parser/withHashMapRules.obj");
            if (url1 == null) {
                throw new DException("DSE0", new Object[] { "withHashMapRules.obj file is missing in classpath" });
            }
        } else {
            try {
                url1 = new URL(url.getProtocol() + ":" + url.getPath() + "/withHashMapRules.obj");
            } catch (MalformedURLException ex) {
                throw new DException("DSE0", new Object[] { ex });
            }
        }
        try {
            ObjectInputStream ooin = new ObjectInputStream(new BufferedInputStream(url1.openStream()));
            withHashMapRules = (ArrayList) ooin.readObject();
            ooin.close();
        } catch (ClassNotFoundException ex1) {
            throw new DException("DSE0", new Object[] { ex1 });
        } catch (IOException ex1) {
            throw new DException("DSE0", new Object[] { ex1 });
        }
    }
