    public SoftMixingMainMixer(SoftMixingMixer mixer) {
        this.mixer = mixer;
        nrofchannels = mixer.getFormat().getChannels();
        int buffersize = (int) (mixer.getFormat().getSampleRate() / mixer.getControlRate());
        control_mutex = mixer.control_mutex;
        buffers = new SoftAudioBuffer[16];
        for (int i = 0; i < buffers.length; i++) {
            buffers[i] = new SoftAudioBuffer(buffersize, mixer.getFormat());
        }
        reverb = new SoftReverb();
        chorus = new SoftChorus();
        agc = new SoftLimiter();
        float samplerate = mixer.getFormat().getSampleRate();
        float controlrate = mixer.getControlRate();
        reverb.init(samplerate, controlrate);
        chorus.init(samplerate, controlrate);
        agc.init(samplerate, controlrate);
        reverb.setMixMode(true);
        chorus.setMixMode(true);
        agc.setMixMode(false);
        chorus.setInput(0, buffers[CHANNEL_EFFECT2]);
        chorus.setOutput(0, buffers[CHANNEL_LEFT]);
        if (nrofchannels != 1) chorus.setOutput(1, buffers[CHANNEL_RIGHT]);
        chorus.setOutput(2, buffers[CHANNEL_EFFECT1]);
        reverb.setInput(0, buffers[CHANNEL_EFFECT1]);
        reverb.setOutput(0, buffers[CHANNEL_LEFT]);
        if (nrofchannels != 1) reverb.setOutput(1, buffers[CHANNEL_RIGHT]);
        agc.setInput(0, buffers[CHANNEL_LEFT]);
        if (nrofchannels != 1) agc.setInput(1, buffers[CHANNEL_RIGHT]);
        agc.setOutput(0, buffers[CHANNEL_LEFT]);
        if (nrofchannels != 1) agc.setOutput(1, buffers[CHANNEL_RIGHT]);
        InputStream in = new InputStream() {

            private SoftAudioBuffer[] buffers = SoftMixingMainMixer.this.buffers;

            private int nrofchannels = SoftMixingMainMixer.this.mixer.getFormat().getChannels();

            private int buffersize = buffers[0].getSize();

            private byte[] bbuffer = new byte[buffersize * (SoftMixingMainMixer.this.mixer.getFormat().getSampleSizeInBits() / 8) * nrofchannels];

            private int bbuffer_pos = 0;

            private byte[] single = new byte[1];

            public void fillBuffer() {
                processAudioBuffers();
                for (int i = 0; i < nrofchannels; i++) buffers[i].get(bbuffer, i);
                bbuffer_pos = 0;
            }

            public int read(byte[] b, int off, int len) {
                int bbuffer_len = bbuffer.length;
                int offlen = off + len;
                byte[] bbuffer = this.bbuffer;
                while (off < offlen) if (available() == 0) fillBuffer(); else {
                    int bbuffer_pos = this.bbuffer_pos;
                    while (off < offlen && bbuffer_pos < bbuffer_len) b[off++] = bbuffer[bbuffer_pos++];
                    this.bbuffer_pos = bbuffer_pos;
                }
                return len;
            }

            public int read() throws IOException {
                int ret = read(single);
                if (ret == -1) return -1;
                return single[0] & 0xFF;
            }

            public int available() {
                return bbuffer.length - bbuffer_pos;
            }

            public void close() {
                SoftMixingMainMixer.this.mixer.close();
            }
        };
        ais = new AudioInputStream(in, mixer.getFormat(), AudioSystem.NOT_SPECIFIED);
    }
