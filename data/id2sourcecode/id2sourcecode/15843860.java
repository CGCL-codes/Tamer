    public Integer readAudioChannels(AudioFile file) {
        String channelString = file.getAudioHeader().getChannels();
        try {
            Integer result = Integer.parseInt(channelString);
            logger.debug("Got channels:{}", result);
            return result;
        } catch (Exception e) {
        }
        channelString = channelString.toLowerCase();
        if (channelString.contains("stereo")) {
            return 2;
        } else if (channelString.contains("mono")) {
            return 1;
        }
        return null;
    }
