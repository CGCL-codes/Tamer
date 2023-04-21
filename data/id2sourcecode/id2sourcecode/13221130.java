    public static void loadStartupPreferences() {
        URL url = Preferences.class.getClassLoader().getResource(DEFAULT_PREFERENCES_RESOURCE_LOCATION);
        try {
            InputStream is = new BufferedInputStream(url.openStream());
            Properties readProps = read(is);
            setProperties(readProps);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Couldn't find the darned input file");
        }
    }
