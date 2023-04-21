    public Pcm2SpeexAudioInputStream(int mode, int quality, final InputStream in, final AudioFormat format, final long length, final int size) {
        super(in, format, length, size);
        granulepos = 0;
        if (streamSerialNumber == 0) streamSerialNumber = new Random().nextInt();
        packetsPerOggPage = DEFAULT_PACKETS_PER_OGG_PAGE;
        packetCount = 0;
        pageCount = 0;
        framesPerPacket = DEFAULT_FRAMES_PER_PACKET;
        int samplerate = (int) format.getSampleRate();
        if (samplerate < 0) samplerate = DEFAULT_SAMPLERATE;
        channels = format.getChannels();
        if (channels < 0) channels = DEFAULT_CHANNELS;
        if (mode < 0) mode = (samplerate < 12000) ? 0 : ((samplerate < 24000) ? 1 : 2);
        this.mode = mode;
        AudioFormat.Encoding encoding = format.getEncoding();
        if (quality < 0) {
            if (encoding instanceof SpeexEncoding) {
                quality = ((SpeexEncoding) encoding).getQuality();
            } else {
                quality = DEFAULT_QUALITY;
            }
        }
        encoder = new SpeexEncoder();
        encoder.init(mode, quality, samplerate, channels);
        if (encoding instanceof SpeexEncoding && ((SpeexEncoding) encoding).isVBR()) {
            setVbr(true);
        } else {
            setVbr(false);
        }
        frameSize = 2 * channels * encoder.getFrameSize();
        comment = "Encoded with " + SpeexEncoder.VERSION;
        first = true;
    }
