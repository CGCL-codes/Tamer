    private void _setupJavadocPanel(ConfigPanel panel) {
        addOptionComponent(panel, newForcedChoiceOptionComponent(OptionConstants.JAVADOC_API_REF_VERSION));
        addOptionComponent(panel, newForcedChoiceOptionComponent(OptionConstants.JAVADOC_ACCESS_LEVEL));
        addOptionComponent(panel, newForcedChoiceOptionComponent(OptionConstants.JAVADOC_LINK_VERSION));
        addOptionComponent(panel, newStringOptionComponent(OptionConstants.JAVADOC_1_5_LINK));
        addOptionComponent(panel, newStringOptionComponent(OptionConstants.JAVADOC_1_6_LINK));
        addOptionComponent(panel, newStringOptionComponent(OptionConstants.JAVADOC_1_7_LINK));
        addOptionComponent(panel, newStringOptionComponent(OptionConstants.JUNIT_LINK));
        VectorStringOptionComponent additionalJavadoc = new VectorStringOptionComponent(OptionConstants.JAVADOC_ADDITIONAL_LINKS, CONFIG_DESCRIPTIONS.get(OptionConstants.JAVADOC_ADDITIONAL_LINKS), this, CONFIG_LONG_DESCRIPTIONS.get(OptionConstants.JAVADOC_ADDITIONAL_LINKS)) {

            protected boolean verify(String s) {
                boolean result = true;
                try {
                    java.net.URL url = new java.net.URL(s + "/allclasses-frame.html");
                    java.io.InputStream urls = url.openStream();
                    java.io.InputStreamReader is = null;
                    java.io.BufferedReader br = null;
                    try {
                        is = new java.io.InputStreamReader(urls);
                        br = new java.io.BufferedReader(is);
                        String line = br.readLine();
                        if (line == null) {
                            result = false;
                        }
                    } finally {
                        if (br != null) {
                            br.close();
                        }
                        if (is != null) {
                            is.close();
                        }
                        if (urls != null) {
                            urls.close();
                        }
                    }
                } catch (java.io.IOException ioe) {
                    result = false;
                }
                if (!result) {
                    JOptionPane.showMessageDialog(ConfigFrame.this, "Could not find the Javadoc at the URL\n" + s, "Error Adding Javadoc", JOptionPane.ERROR_MESSAGE);
                }
                return result;
            }
        };
        addOptionComponent(panel, additionalJavadoc);
        addOptionComponent(panel, newDirectoryOptionComponent(OptionConstants.JAVADOC_DESTINATION, _dirChooser));
        addOptionComponent(panel, javadocCustomParams = newStringOptionComponent(OptionConstants.JAVADOC_CUSTOM_PARAMS));
        panel.displayComponents();
    }
