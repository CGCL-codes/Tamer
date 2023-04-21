    public AudioAttributes setAudioAttributes(MultimediaInfo videoInfo) {
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec(this.AUDIO_CODEC);
        audio.setBitRate(videoInfo.getAudio().getBitRate());
        audio.setSamplingRate(this.AUDIO_SAMPLINGRATE);
        audio.setChannels(videoInfo.getAudio().getChannels());
        audio.setVolume(this.AUDIO_VOLUME);
        return audio;
    }
