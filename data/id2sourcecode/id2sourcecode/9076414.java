    public static Preferences getDefaultPreferences(URL url) throws java.io.FileNotFoundException, java.io.IOException, java.util.prefs.InvalidPreferencesFormatException {
        if (url == null) {
            url = new FedoraUtils().getClass().getResource("fedora.conf");
            new Throwable("using default preferences " + url).printStackTrace();
        }
        Preferences prefs = (Preferences) prefsCache.get(url);
        if (prefs != null) return prefs;
        Class clazz = new FedoraUtils().getClass();
        prefs = Preferences.userRoot().node(clazz.getPackage().getName());
        System.out.println("*** " + clazz.getName() + ".getPreferences: node=" + prefs);
        System.out.println("*** " + clazz.getName() + ".getPreferences: loading & caching prefs from \"" + url + "\"");
        InputStream stream = new BufferedInputStream(url.openStream());
        prefs.importPreferences(stream);
        prefsCache.put(url, prefs);
        stream.close();
        return prefs;
    }
