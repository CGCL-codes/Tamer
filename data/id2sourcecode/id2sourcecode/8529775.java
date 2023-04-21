        @Override
        protected void work() {
            try {
                AudioInputStream in = AudioSystem.getAudioInputStream(new BufferedInputStream(audio.open(), 256 * 1024));
                try {
                    byte[] buffer = new byte[in.available()];
                    in.read(buffer);
                    try {
                        AudioFormat af = in.getFormat();
                        byte[] buffer2 = null;
                        if (af.getSampleSizeInBits() == 8) {
                            if (af.getEncoding() == AudioFormat.Encoding.PCM_UNSIGNED) {
                                for (int i = 0; i < buffer.length; i++) {
                                    buffer[i] = (byte) ((buffer[i] & 0xFF) - 128);
                                }
                            }
                            buffer2 = AudioThread.convert8To16(buffer);
                            af = new AudioFormat(af.getFrameRate(), 16, af.getChannels(), true, false);
                        } else {
                            buffer2 = buffer;
                        }
                        DataLine.Info clipInfo = new DataLine.Info(SourceDataLine.class, af);
                        clip = (SourceDataLine) AudioSystem.getLine(clipInfo);
                        clip.open();
                        setVolume(initialVolume);
                        audioLen = buffer.length / 2;
                        try {
                            barrier.await();
                        } catch (InterruptedException ex) {
                        } catch (BrokenBarrierException ex) {
                        }
                        clip.start();
                        if (skip * 2 < buffer2.length) {
                            clip.write(buffer2, skip * 2, buffer2.length - skip * 2);
                        }
                        clip.drain();
                        clip.close();
                    } catch (LineUnavailableException ex) {
                        ex.printStackTrace();
                    }
                } finally {
                    in.close();
                }
            } catch (UnsupportedAudioFileException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
