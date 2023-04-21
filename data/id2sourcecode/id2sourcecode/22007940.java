    protected void configure(InputStream propertiesFile, String resourceFile, VariableBundle newParentProperties) {
        writableProperties = new Properties();
        if (resourceFile != null) try {
            resources = ResourceBundle.getBundle(resourceFile, Locale.getDefault());
        } catch (MissingResourceException mre) {
            System.err.println("Error loading resource " + mre.getClassName() + mre.getKey() + ":  trying default locale.");
            try {
                resources = ResourceBundle.getBundle(resourceFile, Locale.US);
            } catch (MissingResourceException mreTwo) {
                System.err.println("Unable to load default (US) resource bundle; exiting.");
                System.exit(1);
            }
        } else resources = null;
        properties = new Properties();
        if (propertiesFile != null) try {
            properties.load(propertiesFile);
        } catch (java.io.IOException ioe) {
            System.err.println(ioe.getMessage() + ":  " + propertiesFile);
        }
        List includeStreams = getPropertyAsList("VariableBundle.include", "");
        if (includeStreams != null && includeStreams.size() > 0) {
            for (int i = 0; i < includeStreams.size(); i++) {
                String current = (String) includeStreams.get(i);
                try {
                    if (current != null && !current.equals("")) {
                        java.net.URL url = this.getClass().getResource(current);
                        java.io.InputStream is = url.openStream();
                        properties.load(is);
                    }
                } catch (java.io.IOException ioe) {
                    System.err.println("error including file " + current + ":  " + ioe.getMessage());
                    ioe.printStackTrace();
                }
            }
        }
        parentProperties = newParentProperties;
    }
