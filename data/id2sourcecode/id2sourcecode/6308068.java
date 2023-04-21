        @Override
        protected void convertInPlace(byte[] buffer, int byteOffset, int frameCount) {
            int sampleCount = frameCount * format.getChannels();
            switch(convertType) {
                case UNSIGNED8:
                    TConversionTool.ulaw2pcm8(buffer, byteOffset, sampleCount, false);
                    break;
                case SIGNED8:
                    TConversionTool.ulaw2pcm8(buffer, byteOffset, sampleCount, true);
                    break;
                case ALAW8:
                    TConversionTool.ulaw2alaw(buffer, byteOffset, sampleCount);
                    break;
                default:
                    throw new RuntimeException("FromUlawStream: Call to convertInPlace, " + "but it cannot convert in place. (convertType=" + convertType + ")");
            }
        }
