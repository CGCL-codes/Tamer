    public void getIdeInfo() {
        setIdeName("Eclipse");
        try {
            setIdeVersion(Platform.getBundle("org.eclipse.jdt.ui").getHeaders().get("Bundle-Version").toString());
        } catch (Exception e) {
            AppRegistry.getExceptionLogger().error(e, "Couldn't get Eclipse version", this.getClass());
        }
        try {
            URL url = Platform.getBundle("org.eclipse.jdt").getResource("about.mappings");
            Properties prop = new Properties();
            InputStream in = url.openStream();
            try {
                prop.load(in);
            } finally {
                in.close();
            }
            setIdeBuild(prop.getProperty("0"));
        } catch (Exception e) {
            AppRegistry.getExceptionLogger().error(e, "Couldn't get Eclipse build", this.getClass());
        }
    }
