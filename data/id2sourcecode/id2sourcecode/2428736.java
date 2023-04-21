        @Override
        protected int convert(byte[] inBuffer, byte[] outBuffer, int outByteOffset, int inFrameCount) {
            int sampleCount = inFrameCount * getOriginalStream().getFormat().getChannels();
            switch(conversionType) {
                case CONVERT_SIGN:
                    TConversionTool.convertSign8(inBuffer, 0, outBuffer, outByteOffset, sampleCount);
                    break;
                case CONVERT_BYTE_ORDER16:
                    TConversionTool.swapOrder16(inBuffer, 0, outBuffer, outByteOffset, sampleCount);
                    break;
                case CONVERT_BYTE_ORDER24:
                    TConversionTool.swapOrder24(inBuffer, 0, outBuffer, outByteOffset, sampleCount);
                    break;
                case CONVERT_BYTE_ORDER32:
                    TConversionTool.swapOrder32(inBuffer, 0, outBuffer, outByteOffset, sampleCount);
                    break;
                case CONVERT_16LTO8S:
                    do16BTO8S(inBuffer, 1, outBuffer, outByteOffset, sampleCount);
                    break;
                case CONVERT_16LTO8U:
                    do16BTO8U(inBuffer, 1, outBuffer, outByteOffset, sampleCount);
                    break;
                case CONVERT_16BTO8S:
                    do16BTO8S(inBuffer, 0, outBuffer, outByteOffset, sampleCount);
                    break;
                case CONVERT_16BTO8U:
                    do16BTO8U(inBuffer, 0, outBuffer, outByteOffset, sampleCount);
                    break;
                case CONVERT_8STO16L:
                    do8STO16L(inBuffer, outBuffer, outByteOffset, sampleCount);
                    break;
                case CONVERT_8STO16B:
                    do8STO16B(inBuffer, outBuffer, outByteOffset, sampleCount);
                    break;
                case CONVERT_8UTO16L:
                    do8UTO16L(inBuffer, outBuffer, outByteOffset, sampleCount);
                    break;
                case CONVERT_8UTO16B:
                    do8UTO16B(inBuffer, outBuffer, outByteOffset, sampleCount);
                    break;
                case CONVERT_ONLY_EXPAND_CHANNELS:
                    System.arraycopy(inBuffer, 0, outBuffer, outByteOffset, inFrameCount * getOriginalStream().getFormat().getFrameSize());
                    break;
                case CONVERT_FLOAT:
                    doFloatConversion(inBuffer, 0, outBuffer, outByteOffset, sampleCount);
                    break;
                default:
                    throw new RuntimeException("PCM2PCMStream: Call to convert with unknown conversionType.");
            }
            if (needExpandChannels) {
                expandChannels(outBuffer, outByteOffset, inFrameCount, (getFormat().getSampleSizeInBits() + 7) / 8, getFormat().getChannels());
            }
            return inFrameCount;
        }
