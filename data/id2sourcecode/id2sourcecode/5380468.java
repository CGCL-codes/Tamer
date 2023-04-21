        public static AudioFormat getAudioFormat(byte[] data, SMDIRecvInstance ri) {
            float sr = (int) (1000000000.0F / getPeriodInNS(data));
            int chnls = getChannels(data);
            int bpw = getBitsPerWord(data);
            return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sr, bpw, chnls, AudioConverter.calculatePCMFrameSize(chnls, bpw), sr, true, ri.getPropertiesForSampleHeader(getSampleHeader(data)));
        }
