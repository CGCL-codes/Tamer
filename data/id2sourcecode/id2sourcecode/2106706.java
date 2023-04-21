    public void notifyDisplay() {
        try {
            List<URL> urls = Collections.list(PropertiesUtils.class.getClassLoader().getResources("META-INF/MANIFEST.MF"));
            for (URL url : urls) {
                InputStream is = url.openStream();
                try {
                    Manifest mf = new Manifest(is);
                    if ("Illico".equals(mf.getMainAttributes().get(Attributes.Name.IMPLEMENTATION_TITLE))) {
                        MapUtils.append(Context.getResponse().getWriter(), "\n", "=", mf.getMainAttributes());
                    }
                } finally {
                    is.close();
                }
            }
        } catch (IOException e) {
            throw new Exception(e);
        }
        System.out.println(ManifestUtils.getVersion("org.illico.common.web"));
    }
