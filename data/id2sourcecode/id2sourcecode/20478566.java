            public void run() {
                prop.firePropertyChange("playing", false, true);
                prop.firePropertyChange("title", null, null);
                playerLock.lock();
                Exception exception = null;
                try {
                    AudioFileFormat audioFileFormat = AudioSystem.getAudioFileFormat(url);
                    Map<String, Object> properties = audioFileFormat.properties();
                    if (properties.containsKey("title")) {
                        prop.firePropertyChange("title", null, properties.get("title"));
                    }
                    AudioInputStream in = AudioSystem.getAudioInputStream(url);
                    AudioFormat audioFormat = in.getFormat();
                    AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 16, audioFormat.getChannels(), audioFormat.getChannels() * 2, audioFormat.getSampleRate(), false);
                    AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, in);
                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
                    SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
                    line.open(decodedFormat);
                    rawplay(din, line);
                    in.close();
                } catch (Exception e) {
                    exception = e;
                } finally {
                    playerLock.unlock();
                }
                prop.firePropertyChange("playing", true, false);
                prop.firePropertyChange("title", null, null);
                if (exception != null) {
                    fireAudioError(exception);
                }
            }
