        public synchronized void run() {
            if (log.isTraceEnabled()) {
                log.trace("Starting to play!");
            }
            InputStream currentlyPlaying;
            URL currentURL = url;
            QUIT: while (!quit) {
                try {
                    if (log.isTraceEnabled()) {
                        log.trace("Parking player");
                    }
                    try {
                        wait();
                    } catch (InterruptedException ie) {
                        if (log.isTraceEnabled()) {
                            log.trace("Player was unparked from interruption");
                        }
                    }
                    currentlyPlaying = inputStream;
                    lastInputStream = inputStream;
                    if (currentlyPlaying == null) {
                        if (log.isWarnEnabled()) {
                            log.warn("Input stream is null, yeilding");
                        }
                        Thread.yield();
                        continue QUIT;
                    }
                    if (inputStream != currentlyPlaying) {
                        if (ain != null) {
                            if (log.isTraceEnabled()) {
                                log.trace("Closing existing audio stream ");
                            }
                            ain.close();
                        }
                    }
                    if (ain == null) {
                        if (log.isTraceEnabled()) {
                            log.trace("Creating audio stream from input stream: " + currentlyPlaying.available());
                        }
                        ain = AudioSystem.getAudioInputStream(currentlyPlaying);
                    }
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
                            log.trace("Player -- inputStream changed, stopping");
                            stop = true;
                            break PLAY;
                        }
                        if (pause) {
                            if (log.isTraceEnabled()) {
                                log.trace("pause flag set, pausing player");
                            }
                            try {
                                doPauseCallbacks();
                                wait();
                            } catch (InterruptedException ie) {
                                if (log.isTraceEnabled()) {
                                    log.trace("player was interrupted from pause");
                                }
                            }
                        }
                        try {
                            bytesread = ain.read(buffer, numbytes, buffer.length - numbytes);
                            if (bytesread == -1) {
                                if (log.isTraceEnabled()) {
                                    log.trace("Player -- End of stream reached, stopping");
                                }
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
                            log.error("Failure playing", t);
                            inputStream.close();
                            inputStream = null;
                            currentlyPlaying = null;
                            line.stop();
                            doStopCallbacks();
                        }
                    }
                    if (line.isRunning()) {
                        line.drain();
                    }
                    if (log.isTraceEnabled()) {
                        log.trace("Player stopping line out (done playing?)");
                    }
                    line.stop();
                    stopped = true;
                } catch (UnsupportedAudioFileException e) {
                    log.error("Error playing stream", e);
                } catch (LineUnavailableException e) {
                    log.error("Error playing stream", e);
                } catch (IOException e) {
                    log.error("Error playing stream", e);
                }
                doStopCallbacks();
                if (log.isTraceEnabled()) {
                    log.trace("Player reached end of control loop, starting over");
                }
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
            if (log.isTraceEnabled()) {
                log.trace("Player exiting!");
            }
        }
