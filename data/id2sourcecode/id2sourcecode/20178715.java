    public PeptideShakerWrapper() {
        jarFileName = jarFileName + new Properties().getVersion() + ".jar";
        UtilitiesGUIDefaults.setLookAndFeel();
        try {
            loadUserPreferences();
            if (useStartUpLog) {
                String path = this.getClass().getResource("PeptideShakerWrapper.class").getPath();
                path = path.substring(5, path.indexOf(jarFileName));
                path = path.replace("%20", " ");
                path = path.replace("%5b", "[");
                path = path.replace("%5d", "]");
                File debugOutput = new File(path + "resources/conf/startup.log");
                bw = new BufferedWriter(new FileWriter(debugOutput));
                bw.write("Memory settings read from the user preferences: " + userPreferences.getMemoryPreference() + "\n");
            }
            launch();
            if (useStartUpLog) {
                bw.flush();
                bw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
