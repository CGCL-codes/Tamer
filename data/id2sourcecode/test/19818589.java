    public void audioPreload(URL u) throws javax.sound.sampled.UnsupportedAudioFileException, javax.sound.sampled.LineUnavailableException, java.io.IOException {
        javax.sound.sampled.AudioInputStream aii = javax.sound.sampled.AudioSystem.getAudioInputStream(u);
        javax.sound.sampled.AudioFormat af = aii.getFormat();
        javax.sound.sampled.AudioFormat finalFormat = af;
        javax.sound.sampled.AudioInputStream finalStream = aii;
        if (u.getPath().toLowerCase().endsWith(".ogg") || u.getPath().toLowerCase().endsWith(".mp3")) {
            AudioFormat baseFormat = aii.getFormat();
            finalFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            finalStream = AudioSystem.getAudioInputStream(finalFormat, aii);
        }
        javax.sound.sampled.Clip cl = (javax.sound.sampled.Clip) javax.sound.sampled.AudioSystem.getLine(new javax.sound.sampled.DataLine.Info(javax.sound.sampled.Clip.class, finalFormat));
        cl.open(finalStream);
        audioPreloaded.put(u, cl);
    }
