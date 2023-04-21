    public void open(SourceDataLine line) throws LineUnavailableException {
        if (isOpen()) {
            implicitOpen = false;
            return;
        }
        synchronized (control_mutex) {
            try {
                if (line != null) format = line.getFormat();
                AudioInputStream ais = openStream(getFormat());
                if (line == null) {
                    synchronized (SoftMixingMixerProvider.mutex) {
                        SoftMixingMixerProvider.lockthread = Thread.currentThread();
                    }
                    try {
                        Mixer defaultmixer = AudioSystem.getMixer(null);
                        if (defaultmixer != null) {
                            DataLine.Info idealinfo = null;
                            AudioFormat idealformat = null;
                            Line.Info[] lineinfos = defaultmixer.getSourceLineInfo();
                            idealFound: for (int i = 0; i < lineinfos.length; i++) {
                                if (lineinfos[i].getLineClass() == SourceDataLine.class) {
                                    DataLine.Info info = (DataLine.Info) lineinfos[i];
                                    AudioFormat[] formats = info.getFormats();
                                    for (int j = 0; j < formats.length; j++) {
                                        AudioFormat format = formats[j];
                                        if (format.getChannels() == 2 || format.getChannels() == AudioSystem.NOT_SPECIFIED) if (format.getEncoding().equals(Encoding.PCM_SIGNED) || format.getEncoding().equals(Encoding.PCM_UNSIGNED)) if (format.getSampleRate() == AudioSystem.NOT_SPECIFIED || format.getSampleRate() == 48000.0) if (format.getSampleSizeInBits() == AudioSystem.NOT_SPECIFIED || format.getSampleSizeInBits() == 16) {
                                            idealinfo = info;
                                            int ideal_channels = format.getChannels();
                                            boolean ideal_signed = format.getEncoding().equals(Encoding.PCM_SIGNED);
                                            float ideal_rate = format.getSampleRate();
                                            boolean ideal_endian = format.isBigEndian();
                                            int ideal_bits = format.getSampleSizeInBits();
                                            if (ideal_bits == AudioSystem.NOT_SPECIFIED) ideal_bits = 16;
                                            if (ideal_channels == AudioSystem.NOT_SPECIFIED) ideal_channels = 2;
                                            if (ideal_rate == AudioSystem.NOT_SPECIFIED) ideal_rate = 48000;
                                            idealformat = new AudioFormat(ideal_rate, ideal_bits, ideal_channels, ideal_signed, ideal_endian);
                                            break idealFound;
                                        }
                                    }
                                }
                            }
                            if (idealformat != null) {
                                format = idealformat;
                                line = (SourceDataLine) defaultmixer.getLine(idealinfo);
                            }
                        }
                        if (line == null) line = AudioSystem.getSourceDataLine(format);
                    } finally {
                        synchronized (SoftMixingMixerProvider.mutex) {
                            SoftMixingMixerProvider.lockthread = null;
                        }
                    }
                    if (line == null) throw new IllegalArgumentException("No line matching " + info.toString() + " is supported.");
                }
                double latency = this.latency;
                if (!line.isOpen()) {
                    int bufferSize = getFormat().getFrameSize() * (int) (getFormat().getFrameRate() * (latency / 1000000f));
                    line.open(getFormat(), bufferSize);
                    sourceDataLine = line;
                }
                if (!line.isActive()) line.start();
                int controlbuffersize = 512;
                try {
                    controlbuffersize = ais.available();
                } catch (IOException e) {
                }
                int buffersize = line.getBufferSize();
                buffersize -= buffersize % controlbuffersize;
                if (buffersize < 3 * controlbuffersize) buffersize = 3 * controlbuffersize;
                if (jitter_correction) {
                    ais = new SoftJitterCorrector(ais, buffersize, controlbuffersize);
                }
                pusher = new SoftAudioPusher(line, ais, controlbuffersize);
                pusher_stream = ais;
                pusher.start();
            } catch (LineUnavailableException e) {
                if (isOpen()) close();
                throw new LineUnavailableException(e.toString());
            }
        }
    }
