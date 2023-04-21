    private void intialialiseObfuscatedHashMap() throws DException {
        URL url = getClass().getResource("classes.obj");
        if (url == null) {
            throw new DException("DSE0", new Object[] { "Classes.obj file is missing in classpath." });
        }
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(new BufferedInputStream(url.openStream()));
            mapping = (HashMap) inputStream.readObject();
            inputStream.close();
        } catch (ClassNotFoundException ex) {
            throw new DException("DSE0", new Object[] { ex });
        } catch (IOException ex) {
            throw new DException("DSE0", new Object[] { ex });
        }
        url = getClass().getResource("AB.obj");
        if (url != null) {
            try {
                inputStream = new ObjectInputStream(new BufferedInputStream(url.openStream()));
                obfuscateMapping = (HashMap) inputStream.readObject();
                inputStream.close();
            } catch (ClassNotFoundException ex) {
                throw new DException("DSE0", new Object[] { ex });
            } catch (IOException ex) {
                throw new DException("DSE0", new Object[] { ex });
            }
        }
        if (mapping == null) {
            mapping = new HashMap();
        }
        if (obfuscateMapping == null) {
            obfuscateMapping = new HashMap();
        }
        initialiseQualifiedMapping();
    }
