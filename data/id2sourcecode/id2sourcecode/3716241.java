    protected int checkSize(int mode, int quality, int channels, SpeexEncoder speexEncoder) {
        assertEquals("Number of channels don't match", channels, speexEncoder.getChannels());
        int bitsize = 0;
        if (mode == 0) {
            bitsize = NbCodec.NB_FRAME_SIZE[NbEncoder.NB_QUALITY_MAP[quality]];
            assertEquals("SubModes don't match", NbEncoder.NB_QUALITY_MAP[quality], speexEncoder.getEncoder().getMode());
        } else if (mode == 1) {
            bitsize = NbCodec.NB_FRAME_SIZE[SbEncoder.NB_QUALITY_MAP[quality]];
            bitsize += SbCodec.SB_FRAME_SIZE[SbEncoder.WB_QUALITY_MAP[quality]];
        } else if (mode == 2) {
            bitsize = NbCodec.NB_FRAME_SIZE[SbEncoder.NB_QUALITY_MAP[quality]];
            bitsize += SbCodec.SB_FRAME_SIZE[SbEncoder.WB_QUALITY_MAP[quality]];
            bitsize += SbCodec.SB_FRAME_SIZE[SbEncoder.UWB_QUALITY_MAP[quality]];
        }
        assertEquals("Number of encoded bits don't match", bitsize, speexEncoder.getEncoder().getEncodedFrameSize());
        if (channels > 1) {
            bitsize += 17;
        }
        return (bitsize + 7) / 8;
    }
