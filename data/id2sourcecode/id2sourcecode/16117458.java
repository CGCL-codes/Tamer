    public static double[][] synthesizeAndWriteToBuffer(byte[] buffer, double duration, AudioFormat audio_format, int synthesis_type, double gain, double panning, double fundamental_frequency, double max_frac_samp_rate, double click_avoid_env_length) throws Exception {
        if (audio_format == null) throw new Exception("Null audio format provided.");
        if ((audio_format.getSampleSizeInBits() != 16 && audio_format.getSampleSizeInBits() != 8) || !audio_format.isBigEndian() || audio_format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) throw new Exception("Only 8 or 16 bit signed PCM samples with a big-endian\n" + "byte order can be generated currently.");
        int number_of_channels = audio_format.getChannels();
        float sample_rate = audio_format.getSampleRate();
        int bit_depth = audio_format.getSampleSizeInBits();
        int total_number_of_samples_per_channel = 0;
        if (buffer != null) {
            int bytes_per_sample = bit_depth / 8;
            int total_number_of_bytes = buffer.length;
            int total_number_of_samples = total_number_of_bytes / bytes_per_sample;
            total_number_of_samples_per_channel = total_number_of_samples / number_of_channels;
        } else total_number_of_samples_per_channel = (int) (sample_rate * duration);
        double[][] sample_values = null;
        if (synthesis_type == SINE_WAVE) {
            sample_values = generateSamplesSineWave(fundamental_frequency, number_of_channels, sample_rate, max_frac_samp_rate, total_number_of_samples_per_channel);
        } else if (synthesis_type == BASIC_TONE) {
            sample_values = generateSamplesBasicTone(fundamental_frequency, number_of_channels, sample_rate, max_frac_samp_rate, total_number_of_samples_per_channel);
        } else if (synthesis_type == STEREO_PANNING) {
            sample_values = generateSamplesStereoPanning(fundamental_frequency, number_of_channels, sample_rate, max_frac_samp_rate, total_number_of_samples_per_channel);
        } else if (synthesis_type == STEREO_PINPONG) {
            sample_values = generateSamplesStereoPingpong(fundamental_frequency, number_of_channels, sample_rate, max_frac_samp_rate, total_number_of_samples_per_channel);
        } else if (synthesis_type == FM_SWEEP) {
            sample_values = generateSamplesFMSweep(fundamental_frequency, number_of_channels, sample_rate, max_frac_samp_rate, total_number_of_samples_per_channel);
        } else if (synthesis_type == DECAY_PULSE) {
            sample_values = generateSamplesDecayPulse(fundamental_frequency, number_of_channels, sample_rate, max_frac_samp_rate, total_number_of_samples_per_channel);
        } else if (synthesis_type == WHITE_NOISE) {
            sample_values = generateWhiteNoise(number_of_channels, total_number_of_samples_per_channel);
        } else throw new Exception("Invalid synthesis type specified.");
        applyGainAndPanning(sample_values, gain, panning);
        applyClickAvoidanceAttenuationEnvelope(sample_values, click_avoid_env_length, sample_rate);
        int samples_per_channel = sample_values[0].length;
        for (int chan = 0; chan < sample_values.length; chan++) if (sample_values[chan].length != samples_per_channel) throw new Exception("Channels do not have equal number of samples.");
        if (buffer != null) {
            writeSamplesToBuffer(sample_values, bit_depth, buffer);
            return null;
        } else return sample_values;
    }
