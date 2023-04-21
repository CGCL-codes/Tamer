    public InputStream getSoundStream(String soundName) {
        String sDir = getSoundsDir();
        if (!soundName.startsWith(sDir)) {
            soundName = sDir + soundName;
        }
        if (this.inJar) {
            try {
                URL url = getClassResourceUrl(this.getClass(), soundName);
                if (url != null) {
                    return url.openStream();
                }
            } catch (IOException ioe) {
                Debug.signal(Debug.ERROR, this, ioe);
            }
        } else {
            try {
                return new FileInputStream(soundName);
            } catch (FileNotFoundException fe) {
                Debug.signal(Debug.ERROR, this, fe);
            }
        }
        return null;
    }
