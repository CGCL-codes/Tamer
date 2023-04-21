    public static Preferences getPreferences(Repository repository) throws java.io.FileNotFoundException, java.io.IOException, java.util.prefs.InvalidPreferencesFormatException {
        if (repository.getPrefernces() != null) {
            return repository.getPrefernces();
        } else {
            URL url = repository.getConfiguration();
            Preferences prefs = (Preferences) prefsCache.get(url);
            if (prefs != null) return prefs;
            prefs = Preferences.userRoot().node("/");
            System.out.println("*** FedoraUtils.getPreferences: loading & caching prefs from \"" + url + "\"");
            InputStream stream = new BufferedInputStream(url.openStream());
            prefs.importPreferences(stream);
            prefsCache.put(url, prefs);
            stream.close();
            return prefs;
        }
    }
