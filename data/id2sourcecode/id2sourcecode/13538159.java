    private GenericAudioHeader getAudioHeader(final AsfHeader header) throws CannotReadException {
        final GenericAudioHeader info = new GenericAudioHeader();
        if (header.getFileHeader() == null) {
            throw new CannotReadException("Invalid ASF/WMA file. File header object not available.");
        }
        if (header.getAudioStreamChunk() == null) {
            throw new CannotReadException("Invalid ASF/WMA file. No audio stream contained.");
        }
        info.setBitrate(header.getAudioStreamChunk().getKbps());
        info.setChannelNumber((int) header.getAudioStreamChunk().getChannelCount());
        info.setEncodingType("ASF (audio): " + header.getAudioStreamChunk().getCodecDescription());
        info.setLossless(header.getAudioStreamChunk().getCompressionFormat() == AudioStreamChunk.WMA_LOSSLESS);
        info.setPreciseLength(header.getFileHeader().getPreciseDuration());
        info.setSamplingRate((int) header.getAudioStreamChunk().getSamplingRate());
        info.setVariableBitRate(determineVariableBitrate(header));
        return info;
    }
