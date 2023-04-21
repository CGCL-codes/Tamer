    public AudioSamples getCopyOfAudioSamples() throws Exception {
        String new_unique_ID = null;
        if (new_unique_ID != null) new_unique_ID = new String(unique_ID);
        double[][] new_channel_samples = null;
        if (channel_samples != null) {
            new_channel_samples = new double[channel_samples.length][];
            for (int i = 0; i < new_channel_samples.length; i++) {
                new_channel_samples[i] = new double[channel_samples[i].length];
                for (int j = 0; j < new_channel_samples[i].length; j++) new_channel_samples[i][j] = channel_samples[i][j];
            }
        } else {
            new_channel_samples = new double[1][samples.length];
            for (int i = 0; i < samples.length; i++) new_channel_samples[0][i] = samples[i];
        }
        AudioFormat new_audio_format = null;
        if (audio_format != null) {
            new_audio_format = new AudioFormat(audio_format.getEncoding(), audio_format.getSampleRate(), audio_format.getSampleSizeInBits(), audio_format.getChannels(), audio_format.getFrameSize(), audio_format.getFrameRate(), audio_format.isBigEndian());
        }
        return new AudioSamples(new_channel_samples, new_audio_format, new_unique_ID, false);
    }
