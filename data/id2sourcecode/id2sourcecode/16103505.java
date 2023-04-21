    public void addFile(AudioFile audioFile, String relativePath) {
        statistic.increaseValidFileCount();
        statistic.addStatisticItem(Statistic.MAP_CODEC, audioFile.getEncodingType());
        statistic.addStatisticItem(Statistic.MAP_SAMPLING, "" + audioFile.getSamplingRate());
        statistic.addStatisticItem(Statistic.MAP_BITRATE, "" + audioFile.getBitrate());
        statistic.addStatisticItem(Statistic.MAP_CHANNELS, "" + audioFile.getChannelNumber());
        statistic.addDuration(audioFile.getLength() * 1000);
        statistic.addFileSize(audioFile.length());
    }
