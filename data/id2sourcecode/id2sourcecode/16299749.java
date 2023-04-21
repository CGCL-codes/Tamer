    public void restore() {
        try {
            this.props = SettingsLoader.getSingleton().restore(Settings.propFile);
        } catch (IOException e) {
            this.setDefaults();
            Log.getSingleton().write("#017 There was an error reading the settings from disk - Falling back to defaults");
            e.printStackTrace();
        }
    }
