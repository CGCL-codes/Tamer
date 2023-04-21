    public static void main(String[] args) {
        IStreamCoder enc = IStreamCoder.make(IStreamCoder.Direction.ENCODING);
        enc.setCodec(ICodec.ID.CODEC_ID_MP3);
        enc.setBitRate(128000);
        enc.setSampleRate(44100);
        enc.setSampleFormat(IAudioSamples.Format.FMT_S16);
        enc.setChannels(2);
        IStreamCoder dec = IStreamCoder.make(IStreamCoder.Direction.DECODING);
        dec.setCodec(ICodec.ID.CODEC_ID_AC3);
        dec.setBitRate(128000);
        dec.setSampleRate(44100);
        dec.setSampleFormat(IAudioSamples.Format.FMT_NONE);
        dec.setTimeBase(IRational.make(1, 44100));
        dec.setChannels(2);
        TestAudioSamplesGenerator gen = new TestAudioSamplesGenerator();
        gen.prepare(enc.getChannels(), enc.getSampleRate());
        IAudioSamples dummysamples = IAudioSamples.make(1152, enc.getChannels());
        IAudioSamples decsamples = IAudioSamples.make(dec.getAudioFrameSize(), dec.getChannels(), dec.getSampleFormat());
        enc.open();
        dec.open();
        _log.debug("Decoder AudioFrameSize:" + dec.getAudioFrameSize());
        _log.debug("Encoder AudioFrameSize:" + enc.getAudioFrameSize());
        System.exit(0);
        int encoded_bytes = 0;
        int decoded_bytes = 0;
        int samples_count = 1152 * 4 + 64;
        IPacket outpacket = IPacket.make();
        for (int a = 0; a < 10; a++) {
            gen.fillNextSamples(dummysamples, dummysamples.getMaxSamples());
            _log.debug(dummysamples.toString());
            _log.debug("put packet+" + dummysamples);
            decoded_bytes += dummysamples.getSize();
            encoded_bytes += enc.encodeAudio(outpacket, dummysamples, 0);
            if (outpacket.isComplete()) {
                _log.debug("encode:" + outpacket.toString());
                dec.decodeAudio(decsamples, outpacket, 0);
                _log.debug("encode:" + decsamples.toString());
            }
        }
        boolean encode = true;
        while (encode) {
            encoded_bytes += enc.encodeAudio(outpacket, null, 0);
            if (outpacket.isComplete()) {
                _log.debug("postencodedelay:" + outpacket.toString());
                dumpHex(outpacket.getData(), outpacket.getSize());
            } else {
                encode = false;
            }
        }
    }
