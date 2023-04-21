        public DecodedMpegAudioInputStream(AudioFormat outputFormat, AudioInputStream inputStream) {
            super(outputFormat, AudioSystem.NOT_SPECIFIED);
            m_encodedStream = inputStream;
            m_bitstream = new Bitstream(inputStream);
            m_decoder = new Decoder(null);
            m_oBuffer = new DMAISObuffer(outputFormat.getChannels());
            m_decoder.setOutputBuffer(m_oBuffer);
        }
