    private QualitySequence generateQualities(ChannelGroup channelGroup) {
        int length = (int) basecalls.getLength();
        List<PhredQuality> qualities = new ArrayList<PhredQuality>(length);
        for (int i = 0; i < length; i++) {
            Nucleotide base = basecalls.get(i);
            final byte[] data = channelGroup.getChannel(base).getConfidence().getData();
            if (i == data.length) {
                break;
            }
            qualities.add(PhredQuality.valueOf(data[i]));
        }
        return new EncodedQualitySequence(RUN_LENGTH_CODEC, qualities);
    }
