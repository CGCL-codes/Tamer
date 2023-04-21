    public static AudioInputStream getConvertedStream(AudioInputStream ais, AudioFormat newFormat) throws Exception {
        AudioFormat targetFormat = newFormat;
        AudioFormat sourceFormat = ais.getFormat();
        if (AudioSystem.isConversionSupported(targetFormat, sourceFormat)) {
            ais = AudioSystem.getAudioInputStream(targetFormat, ais);
        } else {
            if (!isPcm(sourceFormat.getEncoding())) {
                AudioFormat intermediateFormat = new AudioFormat(sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), true, false);
                boolean bSupported = AudioSystem.isConversionSupported(intermediateFormat, sourceFormat);
                if (bSupported) {
                    ais = AudioSystem.getAudioInputStream(intermediateFormat, ais);
                    sourceFormat = intermediateFormat;
                }
            }
            if (isPcm(sourceFormat.getEncoding()) && targetFormat.getSampleRate() != sourceFormat.getSampleRate()) {
                AudioFormat intermediateFormat = new AudioFormat(sourceFormat.getEncoding(), targetFormat.getSampleRate(), sourceFormat.getSampleSizeInBits(), sourceFormat.getChannels(), sourceFormat.getFrameSize(), targetFormat.getFrameRate(), sourceFormat.isBigEndian());
                boolean bSupported1 = AudioSystem.isConversionSupported(intermediateFormat, sourceFormat);
                boolean bSupported2 = AudioSystem.isConversionSupported(targetFormat, intermediateFormat);
                if (bSupported1 && bSupported2) {
                    ais = AudioSystem.getAudioInputStream(intermediateFormat, ais);
                    ais = AudioSystem.getAudioInputStream(targetFormat, ais);
                } else {
                    Debug.out("AudioUtils.getConvertedStream(): conversion not supported:");
                    Debug.out("AudioUtils.getConvertedStream(): source: " + sourceFormat);
                    Debug.out("AudioUtils.getConvertedStream(): conversion supported: " + bSupported1);
                    Debug.out("AudioUtils.getConvertedStream(): intermediate: " + intermediateFormat);
                    Debug.out("AudioUtils.getConvertedStream(): conversion supported: " + bSupported2);
                    Debug.out("AudioUtils.getConvertedStream(): target: " + targetFormat);
                }
                throw new Exception("Audio file cannot be converted!");
            } else {
                Debug.out("AudioUtils.getConvertedStream(): conversion not supported:");
                Debug.out("AudioUtils.getConvertedStream(): source:" + sourceFormat);
                Debug.out("AudioUtils.getConvertedStream(): target:" + targetFormat);
                throw new Exception("Audio file cannot be converted!");
            }
        }
        return ais;
    }
