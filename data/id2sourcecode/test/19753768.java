    public void play() {
        if (isPaused) {
            isPaused = false;
            return;
        }
        isStopped = false;
        Decoder decoder = new Decoder();
        SoundStream stream = null;
        try {
            if (file != null) {
                stream = new SoundStream(new FileInputStream(file));
            } else if (url != null) {
                stream = new SoundStream(url.openStream());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "unable to open the sound stream", e);
        }
        if (stream != null) {
            while (true) {
                if (isStopped) {
                    break;
                }
                if (isPaused) {
                    if (source != null) {
                        source.flush();
                    }
                    sourceVolume = volume;
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                    }
                    continue;
                }
                try {
                    Frame frame = stream.readFrame();
                    if (frame == null) {
                        break;
                    }
                    if (source == null) {
                        int frequency = frame.frequency();
                        int channels = frame.mode() == Frame.SINGLE_CHANNEL ? 1 : 2;
                        AudioFormat format = new AudioFormat(frequency, 16, channels, true, false);
                        Line line = AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, format));
                        source = (SourceDataLine) line;
                        source.open(format);
                        source.start();
                        setVolume(source, sourceVolume = 0);
                    }
                    SampleBuffer output = (SampleBuffer) decoder.decodeFrame(frame, stream);
                    short[] buffer = output.getBuffer();
                    int offs = 0;
                    int len = output.getBufferLength();
                    if (sourceVolume != volume) {
                        if (sourceVolume > volume) {
                            sourceVolume -= 10;
                            if (sourceVolume < volume) {
                                sourceVolume = volume;
                            }
                        } else {
                            sourceVolume += 10;
                            if (sourceVolume > volume) {
                                sourceVolume = volume;
                            }
                        }
                        setVolume(source, sourceVolume);
                    }
                    source.write(toByteArray(buffer, offs, len), 0, len * 2);
                    stream.closeFrame();
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "unexpected problems while playing " + toString(), e);
                    break;
                }
            }
            if (source == null) {
                LOGGER.log(Level.INFO, "source is null because first frame is null, so probably the file is not a mp3");
            } else {
                if (!isStopped) {
                    source.drain();
                } else {
                    source.flush();
                }
                source.stop();
                source.close();
                source = null;
            }
            try {
                stream.close();
            } catch (Exception e) {
            }
        }
        isStopped = true;
    }
