    @Override
    void setTags(AudioFile audio, String fileExtension) {
        try {
            this.tfBitrate.setText(audio.getAudioHeader().getBitRate());
        } catch (KeyNotFoundException e) {
            this.tfBitrate.setText("");
        }
        try {
            this.tfChannels.setText(audio.getAudioHeader().getChannels());
        } catch (KeyNotFoundException e) {
            this.tfChannels.setText("");
        }
        try {
            this.tfVersion.setText(audio.getAudioHeader().getFormat());
        } catch (KeyNotFoundException e) {
            this.tfVersion.setText("");
        }
        try {
            this.tfFrequenz.setText(audio.getAudioHeader().getSampleRate());
        } catch (KeyNotFoundException e) {
            this.tfFrequenz.setText("");
        }
        try {
            this.tfLength.setText(String.valueOf(audio.getAudioHeader().getTrackLength()));
        } catch (KeyNotFoundException e) {
            this.tfLength.setText("");
        }
        try {
            this.tfSize.setText(String.valueOf(audio.getFile().length()));
        } catch (KeyNotFoundException e) {
            this.tfSize.setText("");
        }
        try {
            this.tfVBR.setText(String.valueOf(audio.getAudioHeader().isVariableBitRate()));
        } catch (KeyNotFoundException e) {
            this.tfVBR.setText("");
        }
    }
