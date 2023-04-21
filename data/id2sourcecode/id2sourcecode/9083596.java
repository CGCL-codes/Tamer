        public WeakAudioStream(AudioInputStream stream) {
            this.stream = stream;
            weak_stream_link = new WeakReference<AudioInputStream>(stream);
            converter = AudioFloatConverter.getConverter(stream.getFormat());
            samplesize = stream.getFormat().getFrameSize() / stream.getFormat().getChannels();
            framesize = stream.getFormat().getFrameSize();
        }
