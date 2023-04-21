    public static AudioInputStream readAsMono(final AudioFormat desiredFormat, File file) throws UnsupportedAudioFileException, IOException {
        if (desiredFormat.getSampleSizeInBits() != 16) {
            throw new UnsupportedOperationException("Only 16-bit samples are supported at the moment " + "(you requested " + desiredFormat.getSampleSizeInBits() + ")");
        }
        if (desiredFormat.getChannels() != 1) {
            throw new UnsupportedOperationException("Desired number of channels should be 1 " + "(you requested " + desiredFormat.getChannels() + ")");
        }
        AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
        if (fileFormat.getFormat().getChannels() == 1) {
            return AudioSystem.getAudioInputStream(desiredFormat, AudioSystem.getAudioInputStream(file));
        } else if (fileFormat.getFormat().getChannels() == 2) {
            AudioFormat stereoDesiredFormat = new AudioFormat(desiredFormat.getEncoding(), desiredFormat.getSampleRate(), 16, 2, 4, desiredFormat.getFrameRate(), desiredFormat.isBigEndian(), desiredFormat.properties());
            final AudioInputStream stereoIn = AudioSystem.getAudioInputStream(stereoDesiredFormat, AudioSystem.getAudioInputStream(file));
            InputStream mixed = new InputStream() {

                byte[] monobuf = new byte[16384];

                int offset = 0;

                int length = 0;

                long bytesRead = 0;

                @Override
                public int read() throws IOException {
                    if (offset < length) {
                        bytesRead++;
                        return monobuf[offset++] & 0xff;
                    }
                    length = stereoIn.read(monobuf);
                    if (length <= 0) {
                        logger.fine("reached EOF on original input stream (read " + length + " bytes)");
                        return -1;
                    }
                    for (int i = 0; i < length; i += 4) {
                        int lh, ll, rh, rl;
                        if (desiredFormat.isBigEndian()) {
                            lh = monobuf[i + 0];
                            ll = monobuf[i + 1] & 0xff;
                            rh = monobuf[i + 2];
                            rl = monobuf[i + 3] & 0xff;
                        } else {
                            lh = monobuf[i + 1];
                            ll = monobuf[i + 0] & 0xff;
                            rh = monobuf[i + 3];
                            rl = monobuf[i + 2] & 0xff;
                        }
                        int left = (lh << 8 | ll);
                        int right = (rh << 8 | rl);
                        int mixed = (left + right) / 2;
                        if (desiredFormat.isBigEndian()) {
                            monobuf[(i / 2) + 1] = (byte) (mixed & 0xff);
                            monobuf[(i / 2) + 0] = (byte) ((mixed >> 8) & 0xff);
                        } else {
                            monobuf[(i / 2) + 0] = (byte) (mixed & 0xff);
                            monobuf[(i / 2) + 1] = (byte) ((mixed >> 8) & 0xff);
                        }
                    }
                    length /= 2;
                    offset = 0;
                    return monobuf[offset++] & 0xff;
                }

                @Override
                public synchronized void mark(int readlimit) {
                    throw new UnsupportedOperationException("Mark not supported");
                }
            };
            logger.info("Creating 1-channel mixed input stream from stereo source");
            return new AudioInputStream(mixed, desiredFormat, stereoIn.getFrameLength());
        } else {
            throw new UnsupportedAudioFileException("Unsupported number of channels: " + fileFormat.getFormat().getChannels());
        }
    }
