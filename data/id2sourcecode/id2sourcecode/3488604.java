    private SystemFilesLoader() {
        java.util.Properties props = new java.util.Properties();
        try {
            java.net.URL url = new java.net.URL(NewGenLibDesktopRoot.getInstance().getURLRoot() + "/SystemFiles/Env_Var.txt");
            props.load(url.openStream());
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        newgenlibDesktopProperties = props;
    }
