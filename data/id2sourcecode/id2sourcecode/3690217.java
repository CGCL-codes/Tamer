    public void open() {
        if ((!opening) && (!opened) && url != null) {
            opening = true;
            _fireAudioStateEvent(AudioStateEvent.AudioState.OPENING);
            Thread openThread = new Thread() {

                public void run() {
                    decoded_input_stream = null;
                    AudioInputStream input_stream = null;
                    try {
                        System.out.println(url.toString());
                        input_stream = AudioSystem.getAudioInputStream(url);
                    } catch (Exception e) {
                        input_stream = null;
                        e.printStackTrace();
                    }
                    if (input_stream != null) {
                        AudioFormat baseFormat = input_stream.getFormat();
                        AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
                        decoded_input_stream = AudioSystem.getAudioInputStream(decodedFormat, input_stream);
                        DataLine.Info info = new DataLine.Info(Clip.class, decodedFormat);
                        try {
                            clip = (Clip) AudioSystem.getLine(info);
                            clip.addLineListener(new LineListener() {

                                public void update(LineEvent evt) {
                                    LineEvent.Type eventType = evt.getType();
                                    if (eventType == LineEvent.Type.OPEN) {
                                        opened = true;
                                    }
                                    if (eventType == LineEvent.Type.CLOSE) {
                                        opened = false;
                                    }
                                    if (eventType == LineEvent.Type.START) {
                                        playing = true;
                                    }
                                    if (eventType == LineEvent.Type.STOP) {
                                        playing = false;
                                        if (clip.getMicrosecondLength() == clip.getMicrosecondPosition()) {
                                            _fireAudioStateEvent(AudioStateEvent.AudioState.STOPPED);
                                            clip.setMicrosecondPosition(0);
                                        }
                                    }
                                }
                            });
                            clip.open(decoded_input_stream);
                            try {
                                input_stream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Runtime.getRuntime().gc();
                            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                            gainControl.setValue(dB);
                            _fireAudioStateEvent(AudioStateEvent.AudioState.READY);
                            opened = true;
                        } catch (Exception e) {
                            _fireAudioStateEvent(AudioStateEvent.AudioState.OPEN_FAILED);
                            failed = true;
                        }
                    }
                    opening = false;
                }
            };
            openThread.start();
        }
    }
