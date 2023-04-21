    public static boolean isXingFrame(ByteBuffer bb, MPEGFrameHeader mpegFrameHeader) {
        int startPosition = bb.position();
        if (mpegFrameHeader.getVersion() == MPEGFrameHeader.VERSION_1) {
            if (mpegFrameHeader.getChannelMode() == MPEGFrameHeader.MODE_MONO) {
                bb.position(startPosition + MPEG_VERSION_1_MODE_MONO_OFFSET);
            } else {
                bb.position(startPosition + MPEG_VERSION_1_MODE_STEREO_OFFSET);
            }
        } else {
            if (mpegFrameHeader.getChannelMode() == MPEGFrameHeader.MODE_MONO) {
                bb.position(startPosition + MPEG_VERSION_2_MODE_MONO_OFFSET);
            } else {
                bb.position(startPosition + MPEG_VERSION_2_MODE_STEREO_OFFSET);
            }
        }
        header = bb.slice();
        bb.position(startPosition);
        byte[] identifier = new byte[XING_IDENTIFIER_BUFFER_SIZE];
        header.get(identifier);
        if ((!Arrays.equals(identifier, XING_VBR_ID)) && (!Arrays.equals(identifier, XING_CBR_ID))) {
            return false;
        }
        MP3File.logger.finest("Found Xing Frame");
        return true;
    }
