        @Override
        protected int convert(byte[] inBuffer, byte[] outBuffer, int outByteOffset, int inFrameCount) {
            int sampleCount = inFrameCount * getFormat().getChannels();
            switch(convertType) {
                case UNSIGNED8:
                    TConversionTool.pcm82ulaw(inBuffer, 0, outBuffer, outByteOffset, sampleCount, false);
                    break;
                case SIGNED8:
                    TConversionTool.pcm82ulaw(inBuffer, 0, outBuffer, outByteOffset, sampleCount, true);
                    break;
                case BIG_ENDIAN16:
                    TConversionTool.pcm162ulaw(inBuffer, 0, outBuffer, outByteOffset, sampleCount, true);
                    break;
                case LITTLE_ENDIAN16:
                    TConversionTool.pcm162ulaw(inBuffer, 0, outBuffer, outByteOffset, sampleCount, false);
                    break;
                case ALAW8:
                    TConversionTool.alaw2ulaw(inBuffer, 0, outBuffer, outByteOffset, sampleCount);
                    break;
            }
            return inFrameCount;
        }
