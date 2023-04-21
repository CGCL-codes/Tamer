    private void _play() throws Exception {
        int length;
        long startPosition;
        long currentPosition;
        long lastPosition;
        AudioFormat baseFormat;
        AudioFormat decodedFormat;
        AudioFileFormat baseFileFormat;
        AudioInputStream in;
        DataLine.Info newInfo;
        int totLength;
        int dataLength;
        boolean firstTime;
        int waitCounter;
        long skip;
        long skipped;
        int byteLength;
        int headerPos;
        boolean endOfSong;
        Map properties;
        InputStream inputStream;
        if (dataSource instanceof File) {
            try {
                properties = null;
                baseFileFormat = AudioSystem.getAudioFileFormat((File) dataSource);
                if (baseFileFormat != null) {
                    if (baseFileFormat instanceof MpegAudioFileFormat) {
                        properties = ((MpegAudioFileFormat) baseFileFormat).properties();
                        debug("properties = " + properties);
                    }
                }
                inputStream = new BufferedInputStream(new FileInputStream((File) dataSource));
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        } else {
            state = STOP;
            return;
        }
        newSong = false;
        debug("play : " + dataSource);
        in = AudioSystem.getAudioInputStream(inputStream);
        baseFormat = in.getFormat();
        totLength = 0;
        decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
        audioInputStream = AudioSystem.getAudioInputStream(decodedFormat, in);
        newInfo = new DataLine.Info(SourceDataLine.class, decodedFormat);
        openLine(newInfo, decodedFormat);
        startPosition = line.getMicrosecondPosition();
        lastPosition = startPosition;
        dataLength = 1024 * decodedFormat.getFrameSize();
        if (data == null || data.length != dataLength) {
            data = new byte[dataLength];
        }
        line.start();
        endOfSong = false;
        try {
            firstTime = true;
            for (; ; ) {
                length = audioInputStream.read(data, 0, dataLength);
                totLength += length;
                if (length < 0) {
                    state = STOP;
                    if (listener != null) {
                        debug("total length = " + totLength);
                        listener.endOfSong();
                        endOfSong = true;
                    }
                    break;
                }
                if (line.getBufferSize() - line.available() < 10) {
                    debug("underrun !!");
                }
                waitCounter = 0;
                while (line.available() < data.length) {
                    if (state == PAUSE || state == SEEK) {
                        break;
                    }
                    Thread.sleep(1);
                    waitCounter++;
                    if (waitCounter > 20) {
                        debug("line.available = " + line.available() + ", " + data.length);
                    }
                }
                if (state == SEEK) {
                    state = SEEKING;
                    return;
                }
                if (state == SEEKING) {
                    byteLength = getByteLength(properties);
                    debug("byteLength = " + byteLength);
                    if (byteLength > 0) {
                        debug("performing skip");
                        skip = (int) (seek * byteLength);
                        while (skip > 0) {
                            skipped = audioInputStream.skip(skip);
                            if (skipped <= 0) {
                                break;
                            }
                            skip -= skipped;
                            System.out.println("skip = " + skip);
                        }
                        debug("skipped :" + skip);
                    }
                    state = PLAY;
                    continue;
                }
                if (state == PAUSE) {
                    debug("pause detected");
                    line.stop();
                    debug("  line stopped");
                    synchronized (lock) {
                        while (state == PAUSE) {
                            try {
                                lock.wait();
                            } catch (InterruptedException ex) {
                            }
                        }
                    }
                    debug("  line started again");
                    line.start();
                }
                if (!newSong) {
                    line.write(data, 0, length);
                    if (firstTime) {
                        firstTime = false;
                        dataLength = 1024 * decodedFormat.getFrameSize();
                    }
                    currentPosition = line.getMicrosecondPosition();
                    if (listener != null && (currentPosition > lastPosition + 1000000)) {
                        listener.updateCursor((int) ((currentPosition - startPosition) / 1000000));
                        lastPosition = currentPosition;
                    }
                } else {
                    debug("new song detected");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            debug("i'm stopping");
            in.close();
            if (endOfSong) {
                debug("drain");
                line.drain();
            } else {
                line.flush();
                line.stop();
            }
        }
    }
