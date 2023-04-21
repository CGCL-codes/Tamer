    private void writePCMAudioStreamFormat(AVIPCMAudioStreamFormat strf) throws IOException {
        output.writeInt16(strf.getFormatTag());
        output.writeInt16(strf.getChannels());
        output.writeInt32(strf.getSamplesPerSecond());
        output.writeInt32(strf.getAvgBytesPerSec());
        output.writeInt16(strf.getBlockAlign());
        output.writeInt16(strf.getBitsPerSample());
        output.writeInt16(strf.getExtraSize());
        if (strf.getExtraSize() != AVIAudioStreamFormat.AVI_AUDIO_STREAM_FORMAT_EXTRA_SIZE_PCM) throw new IOException();
    }
