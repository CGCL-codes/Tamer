    public static void soundCheck(String path) {
        boolean success = true;
        String message = "";
        Exception exception = null;
        log.info("Performing sound check.\t");
        File file = new File(path);
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(file);
        } catch (Exception e) {
            success = false;
            message = "Exception: Sound: getAudioInputStream: " + e.getMessage();
            exception = e;
        }
        if (success) {
            AudioFormat audioFormat = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            if (!AudioSystem.isLineSupported(info)) {
                AudioFormat sourceFormat = audioFormat;
                AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);
                audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
                audioFormat = audioInputStream.getFormat();
                info = new DataLine.Info(SourceDataLine.class, audioFormat);
                if (!AudioSystem.isLineSupported(info)) {
                    success = false;
                    message = "Error Sound: DataLine not supported";
                }
            }
        }
        if (success) {
            log.info("Sound check passed.");
        } else {
            log.warning("Sound check failed.");
            log.warning("Sound check message: " + message);
            if (exception != null) {
                exception.printStackTrace();
            }
            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
            for (int i = 0; i < mixerInfo.length; i++) {
                log.info("Mixer info:\n" + "Mixer[" + i + "] desc: " + mixerInfo[i].getDescription() + "\n" + "Mixer[" + i + "] name: " + mixerInfo[i].getName() + "\n" + "Mixer[" + i + "] vendor: " + mixerInfo[i].getVendor() + "\n" + "Mixer[" + i + "] version: " + mixerInfo[i].getVersion());
            }
        }
    }
