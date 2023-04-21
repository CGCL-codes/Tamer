    private void testRetrievalOfFiles() throws DException {
        Class cls = getClass();
        URL url1 = null;
        if (url == null) {
            url1 = cls.getResource("/com/daffodilwoods/daffodildb/utils/parser/reservedWord.obj");
            if (url1 == null) throw new DException("DSE0", new Object[] { "Reserveword file is missing in classpath." });
        } else {
            try {
                url1 = new URL(url.getProtocol() + ":" + url.getPath() + "/reservedWord.obj");
            } catch (MalformedURLException ex) {
                throw new DException("DSE0", new Object[] { ex });
            }
        }
        try {
            ObjectInputStream ooin = new ObjectInputStream(new BufferedInputStream(url1.openStream()));
            reservedWord = new ArrayList((Vector) ooin.readObject());
            ooin.close();
        } catch (ClassNotFoundException ex1) {
            ex1.printStackTrace();
            throw new DException("DSE0", new Object[] { ex1 });
        } catch (IOException ex1) {
            ex1.printStackTrace();
            throw new DException("DSE0", new Object[] { ex1 });
        }
        if (url == null) {
            url1 = cls.getResource("/com/daffodilwoods/daffodildb/utils/parser/nonReservedWord.obj");
            if (url1 == null) throw new DException("DSE0", new Object[] { "NonReserveword file is missing in classpath." });
        } else {
            try {
                url1 = new URL(url.getProtocol() + ":" + url.getPath() + "/nonReservedWord.obj");
            } catch (MalformedURLException ex1) {
                ex1.printStackTrace();
                throw new DException("DSE0", new Object[] { ex1 });
            }
        }
        try {
            ObjectInputStream ooin = new ObjectInputStream(new BufferedInputStream(url1.openStream()));
            nonReservedWord = new ArrayList((Vector) ooin.readObject());
            ooin.close();
        } catch (ClassNotFoundException ex1) {
            ex1.printStackTrace();
            throw new DException("DSE0", new Object[] { ex1 });
        } catch (IOException ex1) {
            ex1.printStackTrace();
            throw new DException("DSE0", new Object[] { ex1 });
        }
    }
