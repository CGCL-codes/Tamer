    protected static void spiPreloader(String initializerType, String preloadMasterKey) {
        try {
            Enumeration<URL> nums = ClassLoader.getSystemResources("META-INF/services/autoLoader/" + preloadMasterKey);
            Class<?> cls = null;
            List<String> enumeratedFields = new ArrayList<String>();
            while (nums.hasMoreElements()) {
                URL url = null;
                Properties props = null;
                InputStream is = null;
                url = nums.nextElement();
                is = url.openStream();
                props = new Properties();
                props.load(is);
                is.close();
                if (ValueEvaluator.isSet(props.getProperty(PRE_LOAD_CLASS_DEFINITION))) {
                    enumeratedFields.add(props.getProperty(PRE_LOAD_CLASS_DEFINITION));
                }
                for (String key : Config.getNestedKeys(props, PRE_LOAD_CLASS_DEFINITION + ".")) {
                    enumeratedFields.add(props.getProperty(key));
                }
            }
            for (String clsName : enumeratedFields) {
                try {
                    cls = Class.forName(clsName);
                    if (Logger.isInitialized()) Logger.debug(INTL_PLUGIN_PRELOADED.setArgs(cls.getName(), initializerType));
                } catch (Exception e) {
                    if (Logger.isInitialized()) Logger.warn(INTL_PLUGIN_INITIALIZER_ERROR.setArgs(clsName, initializerType), e);
                }
            }
        } catch (IOException e) {
            Logger.warn(INTL_PRELOAD_CANNOT_FIND_PATH.setArgs(preloadMasterKey, initializerType), e);
        }
    }
