    public void load() {
        if (clip != null || loadedInputStream != null) {
            return;
        }
        try {
            String url = Config.SOUNDS_DIR + soundFileName;
            inputStream = AudioSystem.getAudioInputStream(new File(url));
            if (soundFileName.endsWith(".wav")) {
                clip = AudioSystem.getClip();
                clip.open(inputStream);
                url = null;
                inputStream = null;
            } else if (soundFileName.endsWith(".mp3")) {
                loadedInputStream = null;
                AudioFormat baseFormat = inputStream.getFormat();
                decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
                loadedInputStream = AudioSystem.getAudioInputStream(decodedFormat, inputStream);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
