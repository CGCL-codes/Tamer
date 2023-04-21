    private InputStream findVMProfile() {
        URL url = null;
        String propJavaProfile = System.getProperty(Constants.OSGI_JAVA_PROFILE);
        if (propJavaProfile != null) try {
            url = new URL(propJavaProfile);
        } catch (MalformedURLException e1) {
        }
        if (url == null && vmProfile != null) {
            String javaProfile = vmProfile + ".profile";
            url = systemBundle.getEntry(javaProfile);
            if (url == null) url = getClass().getResource(javaProfile);
        }
        if (url != null) try {
            return url.openStream();
        } catch (IOException e) {
        }
        return null;
    }
