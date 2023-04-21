        public ULawtoPCMInputStream(final AudioInputStream stream) {
            this.stream = stream;
            int bufsize = 65536 * stream.getFormat().getChannels();
            this.bytebuf = new byte[bufsize];
            this.shortbuf = ByteBuffer.allocate(2 * bufsize).order(ByteOrder.LITTLE_ENDIAN);
            shortbuf.limit(0);
        }
