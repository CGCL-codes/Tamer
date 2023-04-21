        public DirectAudioFloatInputStream(AudioInputStream stream) {
            converter = AudioFloatConverter.getConverter(stream.getFormat());
            if (converter == null) {
                AudioFormat format = stream.getFormat();
                AudioFormat newformat;
                AudioFormat[] formats = AudioSystem.getTargetFormats(AudioFormat.Encoding.PCM_SIGNED, format);
                if (formats.length != 0) {
                    newformat = formats[0];
                } else {
                    float samplerate = format.getSampleRate();
                    int samplesizeinbits = format.getSampleSizeInBits();
                    int framesize = format.getFrameSize();
                    float framerate = format.getFrameRate();
                    samplesizeinbits = 16;
                    framesize = format.getChannels() * (samplesizeinbits / 8);
                    framerate = samplerate;
                    newformat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, samplerate, samplesizeinbits, format.getChannels(), framesize, framerate, false);
                }
                stream = AudioSystem.getAudioInputStream(newformat, stream);
                converter = AudioFloatConverter.getConverter(stream.getFormat());
            }
            framesize_pc = stream.getFormat().getFrameSize() / stream.getFormat().getChannels();
            this.stream = stream;
        }
