        public EncodedVorbisAudioInputStream(AudioFormat outputFormat, AudioInputStream inputStream) {
            super(outputFormat, AudioSystem.NOT_SPECIFIED, 262144, 16384);
            if (!LIB_AVAILABLE) {
                throw new IllegalArgumentException("format conversion not supported: ogg/vorbis native library not found.");
            }
            if (TDebug.TraceAudioConverter) TDebug.out(">EncodedVorbisAudioInputStream.<init>(): begin");
            m_decodedStream = inputStream;
            m_abReadbuffer = new byte[READ * getFrameSize()];
            Object property = null;
            property = outputFormat.getProperty("vbr");
            boolean bUseVBR = DEFAULT_VBR;
            if (property instanceof Boolean) {
                bUseVBR = ((Boolean) property).booleanValue();
                if (TDebug.TraceAudioConverter) TDebug.out("using VBR: " + bUseVBR);
            }
            property = outputFormat.getProperty("quality");
            float fQuality = DEFAULT_QUALITY;
            if (property instanceof Integer) {
                fQuality = ((Integer) property).intValue() / 10.0F;
                bUseVBR = true;
                if (TDebug.TraceAudioConverter) TDebug.out("using quality (automatically switching VBR on): " + fQuality);
            }
            int nNominalBitrate = DEFAULT_NOM_BITRATE;
            int nMinBitrate = DEFAULT_MIN_BITRATE;
            int nMaxBitrate = DEFAULT_MAX_BITRATE;
            property = outputFormat.getProperty("bitrate");
            if (property instanceof Integer) {
                nNominalBitrate = ((Integer) property).intValue();
                nMinBitrate = nNominalBitrate;
                nMaxBitrate = nNominalBitrate;
                bUseVBR = false;
                if (TDebug.TraceAudioConverter) TDebug.out("using nominal bitrate (automatically switching VBR off): " + nNominalBitrate);
            }
            property = outputFormat.getProperty("vorbis.min_bitrate");
            if (property instanceof Integer) {
                nMinBitrate = ((Integer) property).intValue();
            }
            property = outputFormat.getProperty("vorbis.max_bitrate");
            if (property instanceof Integer) {
                nMaxBitrate = ((Integer) property).intValue() / 1024;
            }
            m_streamState = new StreamState();
            m_page = new Page();
            m_packet = new Packet();
            m_info = new Info();
            m_comment = new Comment();
            m_dspState = new DspState();
            m_block = new Block();
            m_info.init();
            int nSampleRate = (int) inputStream.getFormat().getSampleRate();
            if (TDebug.TraceAudioConverter) TDebug.out("sample rate: " + nSampleRate);
            if (TDebug.TraceAudioConverter) TDebug.out("channels: " + getChannels());
            if (bUseVBR) {
                if (TDebug.TraceAudioConverter) TDebug.out("using VBR with quality: " + fQuality);
                m_info.encodeInitVBR(getChannels(), nSampleRate, fQuality);
            } else {
                if (TDebug.TraceAudioConverter) TDebug.out("using fixed bitrate(max/nom/min): " + nMaxBitrate + "/" + nNominalBitrate + "/" + nMinBitrate);
                m_info.encodeInit(getChannels(), nSampleRate, nMaxBitrate, nNominalBitrate, nMinBitrate);
            }
            m_comment.init();
            m_comment.addTag("ENCODER", "Tritonus libvorbis wrapper");
            m_dspState.initAnalysis(m_info);
            m_block.init(m_dspState);
            Random random = new Random(System.currentTimeMillis());
            m_streamState.init(random.nextInt());
            Packet header = new Packet();
            Packet header_comm = new Packet();
            Packet header_code = new Packet();
            m_dspState.headerOut(m_comment, header, header_comm, header_code);
            m_streamState.packetIn(header);
            m_streamState.packetIn(header_comm);
            m_streamState.packetIn(header_code);
            while (true) {
                int result = m_streamState.flush(m_page);
                if (result == 0) {
                    break;
                }
                getCircularBuffer().write(m_page.getHeader());
                getCircularBuffer().write(m_page.getBody());
            }
            if (TDebug.TraceAudioConverter) {
                TDebug.out("<EncodedVorbisAudioInputStream.<init>(): end");
            }
        }
