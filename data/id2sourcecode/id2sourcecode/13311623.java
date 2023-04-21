    private PersistableCodeGenerator() throws IOException {
        super();
        packageMgr.addPackage("net.sf.joafip", EnumTransformationType.NONE);
        packageMgr.addPackage("net.sf.joafip.java.util", EnumTransformationType.STORABLE);
        final URL url = classLoaderProvider.getResource("joafip_instrumentation.properties");
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
