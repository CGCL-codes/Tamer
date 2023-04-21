        @Override
        protected void convertInPlace(byte[] buffer, int byteOffset, int frameCount) {
            int sampleCount = frameCount * getFormat().getChannels();
            switch(convertType) {
                case UNSIGNED8:
                    TConversionTool.pcm82alaw(buffer, byteOffset, sampleCount, false);
                    break;
                case SIGNED8:
                    TConversionTool.pcm82alaw(buffer, byteOffset, sampleCount, true);
                    break;
                case ULAW8:
                    TConversionTool.ulaw2alaw(buffer, byteOffset, sampleCount);
                    break;
                default:
                    throw new RuntimeException("ToAlawStream: Call to convertInPlace, but it cannot convert in place.");
            }
        }
