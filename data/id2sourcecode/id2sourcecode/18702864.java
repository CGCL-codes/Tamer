        @Override
        public void onAudioSamples(IAudioSamplesEvent event) {
            IAudioSamples samples = event.getAudioSamples();
            if (audioResampler == null) {
                audioResampler = IAudioResampler.make(2, samples.getChannels(), 44100, samples.getSampleRate());
            }
            if (event.getAudioSamples().getNumSamples() > 0) {
                IAudioSamples out = IAudioSamples.make(samples.getNumSamples(), samples.getChannels());
                audioResampler.resample(out, samples, samples.getNumSamples());
                AudioSamplesEvent asc = new AudioSamplesEvent(event.getSource(), out, event.getStreamIndex());
                super.onAudioSamples(asc);
                out.delete();
            }
        }
