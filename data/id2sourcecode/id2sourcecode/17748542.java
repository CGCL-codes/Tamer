    public List<ComponentDefinition> getStaticLoadComponents(ClassLoader classLoader) throws IOException {
        URL url = fileLoader.findResource("META-INF/MANIFEST.MF");
        if (url == null) {
            return Collections.EMPTY_LIST;
        }
        InputStream manifestStream = url.openStream();
        Manifest manifest = new Manifest(manifestStream);
        String acstring = manifest.getMainAttributes().getValue("Asura-Component");
        if (acstring == null || acstring.trim().length() == 0) {
            return Collections.EMPTY_LIST;
        }
        List<ComponentDefinition> rt = new ArrayList<ComponentDefinition>();
        for (String ac : acstring.split("\\,")) {
            ac = ac.trim();
            if (ac.endsWith(".xml") || ac.endsWith(".XML")) {
                rt.addAll(new XmlComponentLoader(fileLoader.findResource(ac)).getStaticLoadComponents(classLoader));
            } else {
                try {
                    rt.add(ComponentDefinition.create(classLoader.loadClass(ac)));
                } catch (ClassNotFoundException e) {
                    throw new IOException("can not load component " + ac + " from " + fileLoader.getURLs()[0], e);
                }
            }
        }
        manifestStream.close();
        return rt;
    }
