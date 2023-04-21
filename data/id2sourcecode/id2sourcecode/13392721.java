    public static void play(InputStream stream) {
        try {
            AudioInputStream in = AudioSystem.getAudioInputStream(stream);
            AudioInputStream din = null;
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            din = AudioSystem.getAudioInputStream(decodedFormat, in);
            rawplay(decodedFormat, din);
            in.close();
        } catch (UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        } catch (IOException iox) {
            iox.printStackTrace();
        } catch (LineUnavailableException lue) {
            lue.printStackTrace();
        }
    }
