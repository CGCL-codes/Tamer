    protected void storeConsoleSpec(Preferences prefs, ConsoleSpec spec, String prefix) {
        prefs.setString(prefix + "title", spec.getTitle());
        prefs.setString(prefix + "encoding", spec.getEncoding());
        prefs.set(prefix + "channels", consoleManager.encodeConsoleChannelsPref(spec.getChannels()));
        prefs.setString(prefix + "messageRegex", spec.getMessageRegex());
        String window = spec.getWindow();
        if (window.equals(MAIN_WINDOW_NAME)) prefs.setString(prefix + "container.id", ConsoleManager.MAIN_CONTAINER_ID); else {
            int customWindowsCount = prefs.getInt("containers.custom.count", 0);
            for (int i = 0; i < customWindowsCount; i++) {
                String containerId = "custom." + i;
                String title = prefs.getString("containers." + containerId + ".title", "");
                System.out.println("Comparing " + window + " with " + title);
                if (window.equals(title)) {
                    prefs.setString(prefix + "container.id", containerId);
                    break;
                }
            }
        }
    }
