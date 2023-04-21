    public static AudioInputStream getPCMConvertedAudioInputStream(AudioInputStream ais) {
        AudioFormat af = ais.getFormat();
        if ((!af.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) && (!af.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED))) {
            try {
                AudioFormat newFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, af.getSampleRate(), 16, af.getChannels(), af.getChannels() * 2, af.getSampleRate(), Platform.isBigEndian());
                ais = AudioSystem.getAudioInputStream(newFormat, ais);
            } catch (Exception e) {
                if (Printer.err) e.printStackTrace();
                ais = null;
            }
        }
        return ais;
    }
