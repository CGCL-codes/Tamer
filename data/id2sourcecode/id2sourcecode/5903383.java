    private static File updateWebstartPluginsCache() {
        final String homeUrl = System.getProperty(RapidMiner.PROPERTY_HOME_REPOSITORY_URL);
        String dirName;
        try {
            final byte[] md5hash = MessageDigest.getInstance("MD5").digest(homeUrl.getBytes());
            dirName = Base64.encodeBytes(md5hash);
        } catch (NoSuchAlgorithmException e) {
            LogService.getRoot().log(Level.WARNING, "Failed to hash remote url: " + e, e);
            return null;
        }
        File cacheDir = new File(ManagedExtension.getUserExtensionsDir(), dirName);
        cacheDir.mkdirs();
        File readmeFile = new File(cacheDir, "README.txt");
        try {
            Tools.writeTextFile(readmeFile, "This directory contains plugins downloaded from RapidAnalytics instance \n" + "  " + homeUrl + ".\n" + "These plugins are only used if RapidMiner is started via WebStart from this \n" + "server. You can delete the directory if you no longer need the cached plugins.");
        } catch (IOException e1) {
            LogService.getRoot().log(Level.WARNING, "Failed to create file " + readmeFile + ": " + e1, e1);
        }
        Document pluginsDoc;
        try {
            URL pluginsListUrl = new URL(homeUrl + "/RAWS/dependencies/resources.xml");
            pluginsDoc = XMLTools.parse(pluginsListUrl.openStream());
        } catch (Exception e) {
            LogService.getRoot().log(Level.WARNING, "Failed to load extensions list from server: " + e, e);
            return null;
        }
        Set<File> cachedFiles = new HashSet<File>();
        NodeList pluginElements = pluginsDoc.getElementsByTagName("extension");
        boolean errorOccurred = false;
        for (int i = 0; i < pluginElements.getLength(); i++) {
            Element pluginElem = (Element) pluginElements.item(i);
            String pluginName = pluginElem.getTextContent();
            String pluginVersion = pluginElem.getAttribute("version");
            File pluginFile = new File(cacheDir, pluginName + "-" + pluginVersion + ".jar");
            cachedFiles.add(pluginFile);
            if (pluginFile.exists()) {
                LogService.getRoot().log(Level.CONFIG, "Found extension on server: " + pluginName + ". Local cache exists.");
            } else {
                LogService.getRoot().log(Level.CONFIG, "Found extension on server: " + pluginName + ". Downloading to local cache.");
                try {
                    URL pluginUrl = new URL(homeUrl + "/RAWS/dependencies/plugins/" + pluginName);
                    Tools.copyStreamSynchronously(pluginUrl.openStream(), new FileOutputStream(pluginFile), true);
                } catch (Exception e) {
                    LogService.getRoot().log(Level.WARNING, "Failed to download extension from server: " + e, e);
                    errorOccurred = true;
                }
            }
        }
        if (!errorOccurred) {
            for (File file : cacheDir.listFiles()) {
                if (file.getName().equals("README.txt")) {
                    continue;
                }
                if (!cachedFiles.contains(file)) {
                    LogService.getRoot().log(Level.CONFIG, "Deleting obsolete file " + file + " from extension cache.");
                    file.delete();
                }
            }
        }
        return cacheDir;
    }
