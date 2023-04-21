    private void jreLeakPrevention(TreeLogger logger) {
        ImageIO.getCacheDirectory();
        try {
            Class<?> clazz = Class.forName("sun.misc.GC");
            Method method = clazz.getDeclaredMethod("requestLatency", new Class[] { long.class });
            method.invoke(null, Long.valueOf(3600000));
        } catch (ClassNotFoundException e) {
            logger.log(TreeLogger.ERROR, "jreLeakPrevention.gcDaemonFail", e);
        } catch (SecurityException e) {
            logger.log(TreeLogger.ERROR, "jreLeakPrevention.gcDaemonFail", e);
        } catch (NoSuchMethodException e) {
            logger.log(TreeLogger.ERROR, "jreLeakPrevention.gcDaemonFail", e);
        } catch (IllegalArgumentException e) {
            logger.log(TreeLogger.ERROR, "jreLeakPrevention.gcDaemonFail", e);
        } catch (IllegalAccessException e) {
            logger.log(TreeLogger.ERROR, "jreLeakPrevention.gcDaemonFail", e);
        } catch (InvocationTargetException e) {
            logger.log(TreeLogger.ERROR, "jreLeakPrevention.gcDaemonFail", e);
        }
        try {
            Class<?> policyClass = Class.forName("javax.security.auth.Policy");
            Method method = policyClass.getMethod("getPolicy");
            method.invoke(null);
        } catch (ClassNotFoundException e) {
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
            logger.log(TreeLogger.WARN, "jreLeakPrevention.authPolicyFail", e);
        } catch (IllegalArgumentException e) {
            logger.log(TreeLogger.WARN, "jreLeakPrevention.authPolicyFail", e);
        } catch (IllegalAccessException e) {
            logger.log(TreeLogger.WARN, "jreLeakPrevention.authPolicyFail", e);
        } catch (InvocationTargetException e) {
            logger.log(TreeLogger.WARN, "jreLeakPrevention.authPolicyFail", e);
        }
        java.security.Security.getProviders();
        try {
            URL url = new URL("jar:file://dummy.jar!/");
            URLConnection uConn = url.openConnection();
            uConn.setDefaultUseCaches(false);
        } catch (MalformedURLException e) {
            logger.log(TreeLogger.ERROR, "jreLeakPrevention.jarUrlConnCacheFail", e);
        } catch (IOException e) {
            logger.log(TreeLogger.ERROR, "jreLeakPrevention.jarUrlConnCacheFail", e);
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            logger.log(TreeLogger.ERROR, "jreLeakPrevention.xmlParseFail", e);
        }
    }
