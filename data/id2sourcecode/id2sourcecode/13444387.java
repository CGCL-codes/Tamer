    public static final ALoad create(File f) throws AudioException {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(f);
            if (f.getName().toLowerCase().endsWith(".mp3")) {
                AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, ais.getFormat().getSampleRate(), 16, ais.getFormat().getChannels(), ais.getFormat().getChannels() * 2, ais.getFormat().getSampleRate(), false);
                ais = AudioSystem.getAudioInputStream(decodedFormat, ais);
            } else if (f.getName().toLowerCase().endsWith(".ogg")) {
                AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, ais.getFormat().getSampleRate(), 16, ais.getFormat().getChannels(), ais.getFormat().getChannels() * 2, ais.getFormat().getSampleRate(), false);
                try {
                    ais = AudioSystem.getAudioInputStream(decodedFormat, ais);
                } catch (IllegalArgumentException iae) {
                    try {
                        ais = ((FormatConversionProvider) (Class.forName("javazoom.spi.vorbis.sampled.convert.VorbisFormatConversionProvider").newInstance())).getAudioInputStream(decodedFormat, ais);
                    } catch (Exception cnfe) {
                        cnfe.printStackTrace();
                    }
                }
            }
            AudioFormat af = ais.getFormat();
            Debug.println(3, "audioformat = " + af.toString());
            int sl = (int) (ais.getFrameLength() * af.getFrameSize() / af.getChannels() / (af.getSampleSizeInBits() >> 3));
            if (sl < 0) {
                sl = 1;
            }
            Debug.println(3, "sample length = " + sl);
            for (int i = 0; i < classList.size(); i++) {
                ALoad l = classList.get(i);
                if (l.supports(af)) {
                    l = l.duplicate();
                    l.setAudioInputStream(ais, sl);
                    l.setFile(f);
                    return l;
                }
            }
            Debug.println(3, "unsupported audioformat = " + af.toString());
            throw new AudioException("unsupportedAudioFormat");
        } catch (UnsupportedAudioFileException uafe) {
            Debug.printStackTrace(5, uafe);
            throw new AudioException("unsupportedAudioFormat");
        } catch (IOException ioe) {
            Debug.printStackTrace(5, ioe);
            throw new AudioException("unsupportedAudioFormat");
        }
    }
