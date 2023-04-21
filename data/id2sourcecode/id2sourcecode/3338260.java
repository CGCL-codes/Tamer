    public InputStream getBundelTemplateStream(String path) {
        Bundle bundle = Activator.getBundleContext().getBundle();
        String absolutePath = BUNDEL_TEMPLATES + path;
        java.net.URL url = bundle.getEntry(absolutePath);
        InputStream in = null;
        try {
            in = url.openStream();
        } catch (IOException ex) {
            System.err.println(ex);
            ex.printStackTrace();
        }
        return in;
    }
