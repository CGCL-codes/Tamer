        public PCM2PCMStream(AudioInputStream sourceStream, AudioFormat targetFormat, int conversionType) {
            super(sourceStream, new AudioFormat(targetFormat.getEncoding(), sourceStream.getFormat().getSampleRate(), targetFormat.getSampleSizeInBits(), targetFormat.getChannels(), AudioUtils.getFrameSize(targetFormat.getChannels(), targetFormat.getSampleSizeInBits()), sourceStream.getFormat().getFrameRate(), targetFormat.isBigEndian(), targetFormat.properties()));
            if (TDebug.TraceAudioConverter) {
                TDebug.out("PCM2PCMStream: constructor. ConversionType=" + conversionType2Str(conversionType));
            }
            this.conversionType = conversionType;
            needExpandChannels = sourceStream.getFormat().getChannels() < targetFormat.getChannels();
            needMixDown = sourceStream.getFormat().getChannels() > targetFormat.getChannels();
            if (needMixDown && conversionType != CONVERT_FLOAT) {
                throw new IllegalArgumentException("PCM2PCMStream: MixDown only possible with CONVERT_FLOAT");
            }
            if (needMixDown && targetFormat.getChannels() != 1) {
                throw new IllegalArgumentException("PCM2PCMStream: MixDown only possible with target channel count=1");
            }
            if (needExpandChannels && sourceStream.getFormat().getChannels() != 1) {
                throw new IllegalArgumentException("PCM2PCMStream: Expanding channels only possible with source channel count=1");
            }
            if (conversionType == CONVERT_FLOAT) {
                int floatChannels = needExpandChannels ? 1 : targetFormat.getChannels();
                intermediateFloatBufferFormat = new AudioFormat(targetFormat.getEncoding(), sourceStream.getFormat().getSampleRate(), targetFormat.getSampleSizeInBits(), floatChannels, AudioUtils.getFrameSize(floatChannels, targetFormat.getSampleSizeInBits()), sourceStream.getFormat().getFrameRate(), targetFormat.isBigEndian(), targetFormat.properties());
                enableConvertInPlace();
            }
            if (!needExpandChannels && (conversionType == CONVERT_SIGN || conversionType == CONVERT_BYTE_ORDER16 || conversionType == CONVERT_BYTE_ORDER24 || conversionType == CONVERT_BYTE_ORDER32)) {
                enableConvertInPlace();
            }
            enableFloatConversion();
        }
