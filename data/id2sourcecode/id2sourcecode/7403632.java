        @Override
        public void decode(ReadableByteChannel in, ByteBuffer out) throws IOException {
            ByteBuffer buffer = packet.getByteBuffer();
            int read;
            do {
                read = in.read(buffer);
            } while (read >= 0 && buffer.hasRemaining());
            if (buffer.hasRemaining()) throw new RuntimeException("Unexpected end of stream.");
            buffer.flip();
            if ((buffer.get(0) & 0xFC) != 0xA0) {
                for (int i = 0; i < key.length; i++) buffer.put(i, (byte) (buffer.get(i) ^ key[i]));
            }
            int offset = 0;
            while (offset < packet.getSize()) {
                int decoded = decoder.decodeAudio(samples, packet, offset);
                if (decoded < 0) throw new RuntimeException("Error decoding ATRAC3 stream.");
                offset += decoded;
                if (!samples.isComplete()) throw new RuntimeException("Error decoding ATRAC3 stream.");
                byte[] data;
                if (loop) {
                    loop = false;
                    int skip = loopFrameOffset * 2 * decoder.getChannels();
                    data = samples.getData().getByteArray(skip, samples.getSize() - skip);
                } else {
                    data = samples.getData().getByteArray(0, samples.getSize());
                }
                out.put(data);
            }
        }
