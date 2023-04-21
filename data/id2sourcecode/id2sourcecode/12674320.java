    private void loadVersion() {
        try {
            String res = "/" + _package.replace('.', '/') + "/" + VERSION_FILE;
            URL url = getClass().getResource(res);
            if (url != null) {
                Properties prop = new Properties();
                InputStream st = url.openStream();
                try {
                    prop.load(st);
                } finally {
                    st.close();
                }
                _version = prop.getProperty("version");
                _versionBuild = prop.getProperty("version.build");
                _date = prop.getProperty("version.date");
            }
        } catch (IOException e) {
        }
    }
