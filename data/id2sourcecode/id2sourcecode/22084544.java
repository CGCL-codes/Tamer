    public boolean seek(final File seekFile, long startByte) throws IOException {
        long filePointerCount;
        final FileInputStream fis = new FileInputStream(seekFile);
        final FileChannel fc = fis.getChannel();
        ByteBuffer bb = ByteBuffer.allocateDirect(FILE_BUFFER_SIZE);
        fc.position(startByte);
        filePointerCount = startByte;
        fc.read(bb, startByte);
        bb.flip();
        boolean syncFound = false;
        try {
            do {
                if (bb.remaining() <= MIN_BUFFER_REMAINING_REQUIRED) {
                    bb.clear();
                    fc.position(filePointerCount);
                    fc.read(bb, fc.position());
                    bb.flip();
                    if (bb.limit() <= MIN_BUFFER_REMAINING_REQUIRED) {
                        return false;
                    }
                }
                if (MPEGFrameHeader.isMPEGFrame(bb)) {
                    try {
                        if (MP3AudioHeader.logger.isLoggable(Level.FINEST)) {
                            MP3AudioHeader.logger.finest("Found Possible header at:" + filePointerCount);
                        }
                        mp3FrameHeader = MPEGFrameHeader.parseMPEGHeader(bb);
                        syncFound = true;
                        if (XingFrame.isXingFrame(bb, mp3FrameHeader)) {
                            if (MP3AudioHeader.logger.isLoggable(Level.FINEST)) {
                                MP3AudioHeader.logger.finest("Found Possible XingHeader");
                            }
                            try {
                                mp3XingFrame = XingFrame.parseXingFrame();
                            } catch (InvalidAudioFrameException ex) {
                            }
                            break;
                        } else {
                            syncFound = isNextFrameValid(seekFile, filePointerCount, bb, fc);
                            if (syncFound == true) {
                                break;
                            }
                        }
                    } catch (InvalidAudioFrameException ex) {
                    }
                }
                bb.position(bb.position() + 1);
                filePointerCount++;
            } while (!syncFound);
        } catch (EOFException ex) {
            MP3AudioHeader.logger.log(Level.WARNING, "Reached end of file without finding sync match", ex);
            syncFound = false;
        } catch (IOException iox) {
            MP3AudioHeader.logger.log(Level.SEVERE, "IOException occurred whilst trying to find sync", iox);
            syncFound = false;
            throw iox;
        } finally {
            if (fc != null) {
                fc.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        if (MP3AudioHeader.logger.isLoggable(Level.FINEST)) {
            MP3AudioHeader.logger.finer("Return found matching mp3 header starting at" + filePointerCount);
        }
        setFileSize(seekFile.length());
        setMp3StartByte(filePointerCount);
        setTimePerFrame();
        setNumberOfFrames();
        setTrackLength();
        setBitRate();
        setEncoder();
        return syncFound;
    }
