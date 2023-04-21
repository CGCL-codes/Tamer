    private void printAudioStreamFormat(AVIAudioStreamFormat streamFormat) {
        double bitRate = 8.0 * streamFormat.getAvgBytesPerSec() / 1000.0;
        info("  Audio:");
        info("    FormatTag: 0x" + Integer.toHexString(streamFormat.getFormatTag()) + " (" + (streamFormat.getFormatTag() == AVIAudioStreamFormat.AVI_AUDIO_STREAM_FORMAT_TAG_MP3 ? "MP3" : streamFormat.getFormatTag() == AVIAudioStreamFormat.AVI_AUDIO_STREAM_FORMAT_TAG_AC3 ? "AC3" : streamFormat.getFormatTag() == AVIAudioStreamFormat.AVI_AUDIO_STREAM_FORMAT_TAG_PCM ? "PCM" : "Unknown") + ")");
        info("    Channels: " + streamFormat.getChannels() + " channels");
        info("    SamplesPerSecond: " + streamFormat.getSamplesPerSecond() + " samples/second");
        info("    AvgBytesPerSec: " + streamFormat.getAvgBytesPerSec() + " bytes/second (" + bitRate + " Kbps)");
        info("    BlockAlign: " + streamFormat.getBlockAlign() + " samples");
        info("    BitsPerSample: " + streamFormat.getBitsPerSample() + " bits");
        info("    ExtraSize: " + streamFormat.getExtraSize() + " bytes");
        if (streamFormat instanceof AVIMP3AudioStreamFormat) {
            AVIMP3AudioStreamFormat mp3f = (AVIMP3AudioStreamFormat) streamFormat;
            double blockRate = (double) streamFormat.getAvgBytesPerSec() / (double) mp3f.getBlockSize();
            double frameRate = (double) mp3f.getFramesPerBlock() * blockRate;
            info("    MP3 ID: " + mp3f.getID());
            info("    MP3 Flags: 0x" + Integer.toHexString(mp3f.getFlags()));
            info("    MP3 BlockSize: " + mp3f.getBlockSize() + " bytes " + "(" + blockRate + " blocks/second)");
            info("    MP3 FramesPerBlock: " + mp3f.getFramesPerBlock() + " frames (" + frameRate + " frames/second)");
            info("    MP3 CodecDelay: " + mp3f.getCodecDelay() + " samples");
        }
        info();
    }
