        public synchronized void run() {
            log.trace("Starting to play!");
            InputStream currentlyPlaying = inputStream;
            QUIT: while (!quit) {
                try {
                    if (log.isTraceEnabled()) {
                        log.trace("Parking player@" + System.currentTimeMillis());
                    }
                    try {
                        wait();
                    } catch (InterruptedException ie) {
                        log.warn(ie);
                    }
                    if (inputStream == null) {
                        yield();
                        continue QUIT;
                    }
                    ain = AudioSystem.getAudioInputStream(inputStream);
                    format = ain.getFormat();
                    info = new DataLine.Info(SourceDataLine.class, format);
                    if (log.isTraceEnabled()) {
                        log.trace("Got format: " + format);
                    }
                    if (!AudioSystem.isLineSupported(info)) {
                        if (log.isTraceEnabled()) {
                            log.trace("Creating transcoder to PCM");
                        }
                        AudioFormat pcm = new AudioFormat(format.getSampleRate(), 16, format.getChannels(), true, false);
                        ain = AudioSystem.getAudioInputStream(pcm, ain);
                        format = ain.getFormat();
                        info = new DataLine.Info(SourceDataLine.class, format);
                    }
                    log.trace("Getting line");
                    line = (SourceDataLine) AudioSystem.getLine(info);
                    if (log.isTraceEnabled()) {
                        log.trace("Setting format to " + format);
                    }
                    line.open(format);
                    log.trace("Starting line out");
                    line.start();
                    int framesize = format.getFrameSize();
                    if (log.isTraceEnabled()) {
                        log.trace("Got frame size of " + framesize);
                    }
                    byte[] buffer = new byte[4096 * framesize];
                    int numbytes = 0;
                    long played = 0;
                    int bytesread = 0;
                    int bytestowrite = 0;
                    int remaining = 0;
                    if (!line.isRunning()) {
                        line.flush();
                        line.start();
                    }
                    stopped = false;
                    log.trace("About to start playing");
                    PLAY: while (!stop) {
                        if (inputStream != currentlyPlaying) {
                        }
                        if (pause) {
                            try {
                                wait();
                            } catch (InterruptedException ie) {
                                log.warn(ie);
                            }
                        }
                        try {
                            bytesread = ain.read(buffer, numbytes, buffer.length - numbytes);
                            if (bytesread == -1) {
                                log.trace("Player -- End of stream reached, stopping");
                                inputStream.close();
                                inputStream = null;
                                currentlyPlaying = null;
                                break PLAY;
                            }
                            numbytes += bytesread;
                            played += (long) bytesread;
                            bytestowrite = (numbytes / framesize) * framesize;
                            line.write(buffer, 0, bytestowrite);
                            remaining = numbytes - bytestowrite;
                            if (remaining > 0) {
                                System.arraycopy(buffer, bytestowrite, buffer, 0, remaining);
                            }
                            numbytes = remaining;
                        } catch (Throwable t) {
                            log.error(t);
                            inputStream.close();
                            inputStream = null;
                            currentlyPlaying = null;
                            line.stop();
                        }
                    }
                    if (line.isRunning()) {
                        line.drain();
                    }
                    log.trace("Player stopping line out (done playing?)");
                    line.stop();
                    stopped = true;
                } catch (UnsupportedAudioFileException e) {
                    log.error(e);
                } catch (LineUnavailableException e) {
                    log.error(e);
                } catch (IOException e) {
                    log.error(e);
                }
                log.trace("Player reached end of control loop, starting over");
            }
            try {
                if (line != null) {
                    line.close();
                }
            } catch (Exception e) {
                log.error(e);
            }
            try {
                if (ain != null) {
                    ain.close();
                }
            } catch (Exception e) {
                log.error(e);
            }
            log.trace("Player exiting!");
        }
