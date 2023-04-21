    public Sound(URL url) throws SlickException {
        SoundStore.get().init();
        String ref = url.getFile();
        try {
            if (ref.toLowerCase().endsWith(".ogg")) {
                sound = SoundStore.get().getOgg(url.openStream());
            } else if (ref.toLowerCase().endsWith(".wav")) {
                sound = SoundStore.get().getWAV(url.openStream());
            } else if (ref.toLowerCase().endsWith(".aif")) {
                sound = SoundStore.get().getAIF(url.openStream());
            } else if (ref.toLowerCase().endsWith(".xm") || ref.toLowerCase().endsWith(".mod")) {
                sound = SoundStore.get().getMOD(url.openStream());
            } else {
                throw new SlickException("Only .xm, .mod, .aif, .wav and .ogg are currently supported.");
            }
        } catch (Exception e) {
            Log.error(e);
            throw new SlickException("Failed to load sound: " + ref);
        }
    }
