        @Override
        protected void convertInPlace(byte[] buffer, int byteOffset, int frameCount) {
            int sampleCount = frameCount * getFormat().getChannels();
            switch(convertType) {
                case UNSIGNED8:
                    TConversionTool.pcm82ulaw(buffer, byteOffset, sampleCount, false);
                    break;
                case SIGNED8:
                    TConversionTool.pcm82ulaw(buffer, byteOffset, sampleCount, true);
                    break;
                case ALAW8:
                    TConversionTool.alaw2ulaw(buffer, byteOffset, sampleCount);
                    break;
                default:
                    throw new RuntimeException("ToUlawStream: Call to convertInPlace, but it cannot convert in place.");
            }
        }
