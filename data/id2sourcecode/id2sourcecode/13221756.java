        public BytaArrayAudioFloatInputStream(AudioFloatConverter converter, byte[] buffer, int offset, int len) {
            this.converter = converter;
            this.format = converter.getFormat();
            this.buffer = buffer;
            this.buffer_offset = offset;
            framesize_pc = format.getFrameSize() / format.getChannels();
            this.buffer_len = len / framesize_pc;
        }
