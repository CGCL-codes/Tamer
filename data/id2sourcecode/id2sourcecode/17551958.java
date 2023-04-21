    private QualityEncodedGlyphs generateQualities(ChannelGroup channelGroup) {
        List<PhredQuality> qualities = new ArrayList<PhredQuality>(basecalls.length());
        for (int i = 0; i < basecalls.length(); i++) {
            NucleotideGlyph base = NucleotideGlyph.getGlyphFor(basecalls.charAt(i));
            final byte[] data = channelGroup.getChannel(base).getConfidence().getData();
            if (i == data.length) {
                break;
            }
            qualities.add(PhredQuality.valueOf(data[i]));
        }
        return new DefaultQualityEncodedGlyphs(RUN_LENGTH_CODEC, qualities);
    }
