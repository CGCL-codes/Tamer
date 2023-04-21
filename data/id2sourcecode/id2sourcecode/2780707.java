    private PersistableCodeGenerator() throws IOException {
        super();
        final Type type = Type.getType(NotStorableClass.class);
        notStorableClassDesc = type.getDescriptor();
        doNotTransformSet.add(Type.getInternalName(ProxyManager2.class));
        doNotTransformSet2.add("java/lang");
        final URL url = ResourceFinder.getResource("instrumentation.properties");
        InputStream inputStream;
        try {
            inputStream = url.openStream();
        } catch (IOException exception) {
            inputStream = null;
        }
        if (inputStream != null) {
            load(inputStream);
        }
    }
