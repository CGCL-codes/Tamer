    public AudioSamples(double[][] audio_samples, AudioFormat audio_format, String unique_identifier, boolean normalize_if_clipped) throws Exception {
        if (audio_samples == null) throw new Exception("Given audio samples array is empty.");
        for (int chan = 0; chan < audio_samples.length; chan++) if (audio_samples[chan] == null) throw new Exception("One or more channels of given audio samples array is empty.");
        int number_samples = audio_samples[0].length;
        for (int chan = 0; chan < audio_samples.length; chan++) if (audio_samples[chan].length != number_samples) throw new Exception("Different channels of given audio samples array have a\n" + "different number of samples.");
        if (audio_format == null) throw new Exception("Null audio format specified for samples.");
        if (audio_format.getChannels() != audio_samples.length) throw new Exception("The specified samples have " + audio_samples.length + " channels but\n" + "the specified audio format has " + audio_format.getChannels() + " channels.\n" + "These must be the same.");
        unique_ID = unique_identifier;
        samples = DSPMethods.getSamplesMixedDownIntoOneChannel(audio_samples);
        if (audio_samples.length == 1) channel_samples = null; else channel_samples = DSPMethods.getCopyOfSamples(audio_samples);
        this.audio_format = AudioMethods.getConvertedAudioFormat(audio_format);
        if (normalize_if_clipped) normalizeIfClipped();
    }
