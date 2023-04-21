    @SuppressWarnings("unchecked")
    public static void initialize() {
        if (repository == null) {
            try {
                Properties jcrProperties = new Properties();
                InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("jcr.properties");
                if (input == null) {
                    throw new RuntimeException("Couldn't load jcr.properties");
                }
                jcrProperties.load(input);
                String repName = jcrProperties.getProperty(PROP_REPOSITORY_NAME, "repo");
                String repHome = System.getProperty("user.home") + "/" + jcrProperties.getProperty(PROP_REPOSITORY_HOME, ".quickwcm");
                String resConfig = repHome + "/repository.xml";
                if (!new File(repHome).exists()) {
                    new File(repHome).mkdir();
                }
                if (!new File(resConfig).exists()) {
                    InputStream repositoryIS = Thread.currentThread().getContextClassLoader().getResourceAsStream("org/apache/jackrabbit/core/repository.xml");
                    OutputStream os = new FileOutputStream(resConfig);
                    int read;
                    byte[] buff = new byte[102400];
                    while ((read = repositoryIS.read(buff)) > 0) {
                        os.write(buff, 0, read);
                    }
                    repositoryIS.close();
                    os.close();
                }
                Hashtable env = new Hashtable();
                env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.jackrabbit.core.jndi.provider.DummyInitialContextFactory");
                env.put(Context.PROVIDER_URL, "localhost");
                InitialContext ctx = new InitialContext(env);
                RegistryHelper.registerRepository(ctx, repName, resConfig, repHome, true);
                repository = (Repository) ctx.lookup(repName);
            } catch (Exception e) {
                e.printStackTrace(System.out);
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }
