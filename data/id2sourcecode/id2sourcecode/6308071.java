        @Override
        protected void convertInPlace(byte[] buffer, int byteOffset, int frameCount) {
            int sampleCount = frameCount * format.getChannels();
            switch(convertType) {
                case UNSIGNED8:
                    TConversionTool.alaw2pcm8(buffer, byteOffset, sampleCount, false);
                    break;
                case SIGNED8:
                    TConversionTool.alaw2pcm8(buffer, byteOffset, sampleCount, true);
                    break;
                case ULAW8:
                    TConversionTool.alaw2ulaw(buffer, byteOffset, sampleCount);
                    break;
                default:
                    throw new RuntimeException("FromAlawStream: Call to convertInPlace, " + "but it cannot convert in place. (convertType=" + convertType + ")");
            }
        }
