        public EncodedVorbisAudioInputStream(AudioFormat outputFormat, AudioInputStream inputStream) {
            super(outputFormat, AudioSystem.NOT_SPECIFIED, 262144, 16384);
            if (TDebug.TraceAudioConverter) {
                TDebug.out(">EncodedVorbisAudioInputStream.<init>(): begin");
            }
            m_decodedStream = inputStream;
            m_abReadbuffer = new byte[READ * getFrameSize()];
            Object property = null;
            property = outputFormat.getProperty("vbr");
            boolean bUseVBR = DEFAULT_VBR;
            if (property instanceof Boolean) {
                bUseVBR = ((Boolean) property).booleanValue();
            }
            property = outputFormat.getProperty("quality");
            float fQuality = DEFAULT_QUALITY;
            if (property instanceof Integer) {
                fQuality = ((Integer) property).intValue() / 10.0F;
            }
            property = outputFormat.getProperty("bitrate");
            int nNominalBitrate = DEFAULT_NOM_BITRATE;
            if (property instanceof Integer) {
                nNominalBitrate = ((Integer) property).intValue() / 1024;
            }
            property = outputFormat.getProperty("vorbis.min_bitrate");
            int nMinBitrate = DEFAULT_MIN_BITRATE;
            if (property instanceof Integer) {
                nMinBitrate = ((Integer) property).intValue() / 1024;
            }
            property = outputFormat.getProperty("vorbis.max_bitrate");
            int nMaxBitrate = DEFAULT_MAX_BITRATE;
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
            if (TDebug.TraceAudioConverter) {
                TDebug.out("sample rate: " + nSampleRate);
            }
            if (TDebug.TraceAudioConverter) {
                TDebug.out("channels: " + getChannels());
            }
            if (bUseVBR) {
                m_info.encodeInitVBR(getChannels(), nSampleRate, fQuality);
            } else {
                m_info.encodeInit(getChannels(), nSampleRate, nMaxBitrate, nNominalBitrate, nMinBitrate);
            }
            m_comment.init();
            m_comment.addTag("ENCODER", "Tritonus libvorbis wrapper");
            property = outputFormat.getProperty("vorbis.comments");
            if (property instanceof List) {
                if (TDebug.TraceAudioConverter) {
                    TDebug.out("VorbisFormatConversionProvider.<init>(): comments present in target format");
                }
                List<?> comments = (List<?>) property;
                for (int i = 0; i < comments.size(); i++) {
                    Object comm = comments.get(i);
                    if (comm instanceof String) {
                        m_comment.addComment((String) comm);
                    }
                }
            }
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
