    private Player resolvePlayer(String soundName) {
        String soundLocation = null;
        if (soundLocations != null) {
            soundLocation = soundLocations.get(soundName);
        }
        if (soundLocation == null) {
            if (logger.isInfoEnabled()) logger.info("No soundlocation defined for sound {}.", soundName);
            return null;
        }
        InputStream soundStream = JLayerSounds.class.getResourceAsStream(soundLocation);
        if (soundStream == null) {
            if (logger.isInfoEnabled()) logger.info("Couldn't retrieve {} as a resource...", soundLocation);
            File file = new File(soundLocation);
            if (file.isFile()) {
                try {
                    soundStream = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    if (logger.isInfoEnabled()) logger.info("Couldn't open {} as a file.", soundLocation);
                }
            }
            if (soundStream == null) {
                try {
                    URL url = new URL(soundLocation);
                    soundStream = url.openStream();
                } catch (MalformedURLException e) {
                    if (logger.isInfoEnabled()) logger.info("Couldn't open {} as a URL.", soundLocation);
                } catch (IOException e) {
                    if (logger.isInfoEnabled()) logger.info("Couldn't open {} as a URL.", soundLocation);
                }
            }
        }
        if (soundStream != null) {
            try {
                return new Player(soundStream);
            } catch (JavaLayerException ex) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Exception while creating player for sound '" + soundName + "'!", ex);
                }
            }
        }
        return null;
    }
