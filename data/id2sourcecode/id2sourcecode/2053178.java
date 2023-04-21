        public AudioFloatInputStreamResampler(AudioFloatInputStream ais, AudioFormat format) {
            this.ais = ais;
            AudioFormat sourceFormat = ais.getFormat();
            targetFormat = new AudioFormat(sourceFormat.getEncoding(), format.getSampleRate(), sourceFormat.getSampleSizeInBits(), sourceFormat.getChannels(), sourceFormat.getFrameSize(), format.getSampleRate(), sourceFormat.isBigEndian());
            nrofchannels = targetFormat.getChannels();
            Object interpolation = format.getProperty("interpolation");
            if (interpolation != null && (interpolation instanceof String)) {
                String resamplerType = (String) interpolation;
                if (resamplerType.equalsIgnoreCase("point")) this.resampler = new SoftPointResampler();
                if (resamplerType.equalsIgnoreCase("linear")) this.resampler = new SoftLinearResampler2();
                if (resamplerType.equalsIgnoreCase("linear1")) this.resampler = new SoftLinearResampler();
                if (resamplerType.equalsIgnoreCase("linear2")) this.resampler = new SoftLinearResampler2();
                if (resamplerType.equalsIgnoreCase("cubic")) this.resampler = new SoftCubicResampler();
                if (resamplerType.equalsIgnoreCase("lanczos")) this.resampler = new SoftLanczosResampler();
                if (resamplerType.equalsIgnoreCase("sinc")) this.resampler = new SoftSincResampler();
            }
            if (resampler == null) resampler = new SoftLinearResampler2();
            pitch[0] = sourceFormat.getSampleRate() / format.getSampleRate();
            pad = resampler.getPadding();
            pad2 = pad * 2;
            ibuffer = new float[nrofchannels][buffer_len + pad2];
            ibuffer2 = new float[nrofchannels * buffer_len];
            ibuffer_index = buffer_len + pad;
            ibuffer_len = buffer_len;
        }
