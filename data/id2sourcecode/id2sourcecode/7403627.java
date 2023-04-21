        public ATRAC3DecoderImpl(Sound sound, ReadableByteChannel in) throws IOException {
            this.sound = sound;
            frameSize = 192 * sound.getChannels();
            loopFrame = sound.getLoopPoint() / 1024;
            loopFrameOffset = sound.getLoopPoint() % 1024;
            decoder = IStreamCoder.make(Direction.DECODING);
            decoder.setSampleRate(sound.getSampleRate());
            decoder.setChannels(sound.getChannels());
            decoder.setCodec(ID.CODEC_ID_ATRAC3);
            decoder.setBitRate(frameSize * 8 * 44100 / 1024);
            decoder.setProperty("block_align", frameSize);
            IBuffer extraData = IBuffer.make(decoder, 14);
            ByteBuffer extraDataBuffer = extraData.getByteBuffer(0, 14);
            extraDataBuffer.putShort((short) 1);
            extraDataBuffer.putInt(sound.getSampleRate());
            extraDataBuffer.putInt(0);
            extraDataBuffer.putInt(1);
            decoder.setExtraData(extraData, 0, 14, true);
            if (decoder.open() < 0) throw new RuntimeException("Error opening ATRAC3 decoder.");
            packet = IPacket.make();
            IBuffer data = IBuffer.make(packet, frameSize);
            packet.setData(data);
            samples = IAudioSamples.make(1024, 2);
            ByteBuffer buffer = packet.getByteBuffer();
            int read;
            do {
                read = in.read(buffer);
            } while (read >= 0 && buffer.hasRemaining());
            if (buffer.hasRemaining()) throw new UnsupportedFormatException("Could not read ATRAC3 audio key.");
            buffer.flip();
            if (buffer.order() == ByteOrder.LITTLE_ENDIAN) {
                for (int i = 0; i < sound.getChannels(); i++) buffer.putInt(i * 192, buffer.getInt(i * 192) ^ 0x9F4E02A0);
            } else {
                for (int i = 0; i < sound.getChannels(); i++) buffer.putInt(i * 192, buffer.getInt(i * 192) ^ 0xA0024E9F);
            }
            buffer.get(key = new byte[frameSize]);
        }
