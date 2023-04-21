    public DecodedMpegAudioInputStream(AudioFormat outputFormat, AudioInputStream inputStream) {
        super(outputFormat, -1);
        if (TDebug.TraceAudioConverter) {
            TDebug.out(">DecodedMpegAudioInputStream(AudioFormat outputFormat, AudioInputStream inputStream)");
        }
        try {
            byteslength = inputStream.available();
        } catch (IOException e) {
            TDebug.out("DecodedMpegAudioInputStream : Cannot run inputStream.available() : " + e.getMessage());
            byteslength = -1;
        }
        m_encodedStream = inputStream;
        shoutlst = IcyListener.getInstance();
        shoutlst.reset();
        m_bitstream = new Bitstream(inputStream);
        m_decoder = new Decoder(null);
        m_equalizer = new Equalizer();
        m_equalizer_values = new float[32];
        for (int b = 0; b < m_equalizer.getBandCount(); b++) {
            m_equalizer_values[b] = m_equalizer.getBand(b);
        }
        m_decoder.setEqualizer(m_equalizer);
        m_oBuffer = new DMAISObuffer(outputFormat.getChannels());
        m_decoder.setOutputBuffer(m_oBuffer);
        try {
            m_header = m_bitstream.readFrame();
            if ((m_header != null) && (frameslength == -1) && (byteslength > 0)) frameslength = m_header.max_number_of_frames((int) byteslength);
        } catch (BitstreamException e) {
            TDebug.out("DecodedMpegAudioInputStream : Cannot read first frame : " + e.getMessage());
            byteslength = -1;
        }
        properties = new HashMap();
    }
