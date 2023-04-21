    private void changeEncodingFormat() {
        try {
            AudioFormat new_audio_format = audio_format_selector.getAudioFormat(false);
            if ((new_audio_format.getSampleSizeInBits() != 16 && new_audio_format.getSampleSizeInBits() != 8) || !new_audio_format.isBigEndian() || new_audio_format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) throw new Exception("Only 8 or 16 bit signed PCM samples with a big-endian\n" + "byte order can be used currently.");
            AudioInputStream original_audio_input_stream = processed_audio_samples.getAudioInputStreamChannelSegregated();
            int original_number_channels = original_audio_input_stream.getFormat().getChannels();
            if (original_number_channels != new_audio_format.getChannels()) throw new Exception("Original audio has " + original_number_channels + " channels but the\n" + "new format has " + new_audio_format.getChannels() + "channels.\n" + "These must match.");
            AudioInputStream new_audio_input_stream = AudioSystem.getAudioInputStream(new_audio_format, original_audio_input_stream);
            processed_audio_samples = new AudioSamples(new_audio_input_stream, processed_audio_samples.getUniqueIdentifier(), false);
            updateRecordingInformation();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
