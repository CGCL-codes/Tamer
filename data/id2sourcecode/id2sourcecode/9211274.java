    private UniqueVector getVoiceDirectoryNamesFromJarURLs(UniqueVector urls) {
        try {
            UniqueVector voiceDirectoryNames = new UniqueVector();
            for (int i = 0; i < urls.size(); i++) {
                JarURLConnection jarConnection = (JarURLConnection) ((URL) urls.get(i)).openConnection();
                Attributes attributes = jarConnection.getMainAttributes();
                String mainClass = attributes.getValue(Attributes.Name.MAIN_CLASS);
                if (mainClass == null || mainClass.trim().equals("")) {
                    throw new Error("No Main-Class found in jar " + (URL) urls.get(i));
                }
                voiceDirectoryNames.add(mainClass);
            }
            return voiceDirectoryNames;
        } catch (IOException e) {
            throw new Error("Error reading jarfile manifests. ");
        }
    }
