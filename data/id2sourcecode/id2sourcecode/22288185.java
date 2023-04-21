    public void loadDictionary(String filepath) {
        CMLDictionary dictionary = null;
        URL url = getClass().getClassLoader().getResource(filepath);
        System.out.println(url.getPath());
        if (url == null) {
            throw new RuntimeException("BUG: can't load dictionary (check resource directories are on the classpath: " + filepath);
        }
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            Document cmldoc = new CMLBuilder().build(inputStream);
            CMLCml cml = (CMLCml) cmldoc.getRootElement();
            dictionary = (CMLDictionary) cml.getFirstCMLChild(CMLDictionary.TAG);
            if (dictionary == null) {
                throw new RuntimeException("Failed to find dictionary element in " + filepath);
            } else {
                dictNSIndex.put(cml.getNamespaceURI(), dictionary);
            }
        } catch (Exception e) {
            throw new RuntimeException("BUG: could not read/parse dictionary: " + filepath, e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
