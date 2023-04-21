    private void getDependencyURLs(URL url, UniqueVector dependencyURLs) {
        try {
            String urlDirName = getURLDirName(url);
            if (url.getProtocol().equals("jar")) {
                JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
                Attributes attributes = jarConnection.getMainAttributes();
                String fullClassPath = attributes.getValue(Attributes.Name.CLASS_PATH);
                if (fullClassPath == null || fullClassPath.equals("")) {
                    return;
                }
                String[] classPath = fullClassPath.split("\\s+");
                URL classPathURL;
                for (int i = 0; i < classPath.length; i++) {
                    try {
                        if (classPath[i].endsWith("/")) {
                            classPathURL = new URL("file:" + urlDirName + classPath[i]);
                        } else {
                            classPathURL = new URL("jar", "", "file:" + urlDirName + classPath[i] + "!/");
                        }
                    } catch (MalformedURLException e) {
                        System.err.println("Warning: unable to resolve dependency " + classPath[i] + " referenced by " + url);
                        continue;
                    }
                    if (!dependencyURLs.contains(classPathURL)) {
                        dependencyURLs.add(classPathURL);
                        getDependencyURLs(classPathURL, dependencyURLs);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
