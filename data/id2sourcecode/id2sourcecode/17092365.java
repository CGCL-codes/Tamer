    private static void playAudioInputStream(final AudioInputStream audioInputStream) {
        assert audioInputStream != null : "audioInputStream is null";
        AudioInputStream din = null;
        try {
            final AudioFormat baseFormat = audioInputStream.getFormat();
            final AudioFormat decodedFormat = new AudioFormat(PCM_SIGNED, baseFormat.getSampleRate(), PCM_CHANNELS_NUMBER, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), baseFormat.isBigEndian());
            din = AudioSystem.getAudioInputStream(decodedFormat, audioInputStream);
            rawplay(decodedFormat, din);
        } catch (final IOException e) {
            LOGGER.log(SEVERE, PLAY_AUDIO_STREAM_ERROR, e);
        } catch (final LineUnavailableException e) {
            LOGGER.log(SEVERE, PLAY_AUDIO_STREAM_ERROR, e);
        } finally {
            if (din != null) {
                try {
                    din.close();
                } catch (final IOException e) {
                    LOGGER.log(SEVERE, CLOSE_AUDIO_STREAM_ERROR, e);
                }
            }
        }
    }
