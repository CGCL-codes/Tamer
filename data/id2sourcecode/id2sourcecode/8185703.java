    protected AudioRecord newAudioRecord(RecordFormat recordFormat) {
        AudioRecord audioRecord = new AudioRecord(AUDIO_SOURCE, recordFormat.getSampleRate(), recordFormat.getChannelConfiguration(), recordFormat.getAudioEncoding(), recordFormat.getBufferSize());
        return audioRecord;
    }
