        @Override
        protected int convert(byte[] inBuffer, byte[] outBuffer, int outByteOffset, int inFrameCount) {
            int sampleCount = inFrameCount * getFormat().getChannels();
            switch(convertType) {
                case UNSIGNED8:
                    TConversionTool.alaw2pcm8(inBuffer, 0, outBuffer, outByteOffset, sampleCount, false);
                    break;
                case SIGNED8:
                    TConversionTool.alaw2pcm8(inBuffer, 0, outBuffer, outByteOffset, sampleCount, true);
                    break;
                case BIG_ENDIAN16:
                    TConversionTool.alaw2pcm16(inBuffer, 0, outBuffer, outByteOffset, sampleCount, true);
                    break;
                case LITTLE_ENDIAN16:
                    TConversionTool.alaw2pcm16(inBuffer, 0, outBuffer, outByteOffset, sampleCount, false);
                    break;
                case ULAW8:
                    TConversionTool.alaw2ulaw(inBuffer, 0, outBuffer, outByteOffset, sampleCount);
                    break;
            }
            return inFrameCount;
        }
