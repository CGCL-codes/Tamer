        @Override
        protected void convertInPlace(byte[] buffer, int byteOffset, int frameCount) {
            int sampleCount = frameCount * getOriginalStream().getFormat().getChannels();
            switch(conversionType) {
                case CONVERT_SIGN:
                    TConversionTool.convertSign8(buffer, byteOffset, sampleCount);
                    break;
                case CONVERT_BYTE_ORDER16:
                    TConversionTool.swapOrder16(buffer, byteOffset, sampleCount);
                    break;
                case CONVERT_BYTE_ORDER24:
                    TConversionTool.swapOrder24(buffer, byteOffset, sampleCount);
                    break;
                case CONVERT_BYTE_ORDER32:
                    TConversionTool.swapOrder32(buffer, byteOffset, sampleCount);
                    break;
                case CONVERT_FLOAT:
                    doFloatConversion(buffer, byteOffset, buffer, byteOffset, sampleCount);
                    if (needExpandChannels) {
                        expandChannels(buffer, byteOffset, frameCount, (getFormat().getSampleSizeInBits() + 7) / 8, getFormat().getChannels());
                    }
                    break;
                default:
                    throw new RuntimeException("PCM2PCMStream: Call to convertInPlace, but it cannot convert in place.");
            }
        }
