    public void addAnnotatedClasses(Bundle sourceBundle, String[] classes) {
        for (String s : classes) {
            try {
                logger.error("Adding class: " + s);
                annotatedClasses.add(sourceBundle.loadClass(s));
                URL url = sourceBundle.getResource("it/incodice/regola/examples/model/Language.hbm.xml");
                InputStream inputStream = url.openStream();
                mappings.add(inputStream);
            } catch (ClassNotFoundException e) {
                logger.error("Error adding annotaded class: " + s, e);
                throw new RuntimeException(e);
            } catch (IOException e) {
                logger.error("Error adding mapping resources: " + s, e);
                throw new RuntimeException(e);
            }
        }
        createNewSessionFactory();
    }
